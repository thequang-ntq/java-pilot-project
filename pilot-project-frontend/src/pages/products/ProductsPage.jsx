import { useState, useEffect, useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import MainLayout from "../../components/layout/MainLayout/MainLayout";
import "./ProductsPage.css";
import Pagination from "../../components/common/Pagination/Pagination";
import { getProducts, deleteProduct } from "../../services/products-api";
import useAuth from "../../components/context/use-auth";
import NoProductImage from "../../assets/no-product-image.jpeg";
import { BASE_URL } from "../../utils/constants";
import { formatPrice, formatDate, filterInput } from "../../utils/utils";
import { toast, Bounce } from "react-toastify";

export default function ProductsPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const { auth } = useAuth();
  const isAdmin = auth?.role === "ADMIN";

  const [search, setSearch] = useState("");
  const [priceFrom, setPriceFrom] = useState("");
  const [priceTo, setPriceTo] = useState("");
  const [appliedFilters, setAppliedFilters] = useState({
    search: "",
    priceFrom: "",
    priceTo: "",
  });
  const [page, setPage] = useState(1);
  const [deleteModal, setDeleteModal] = useState(null);
  const [detailModal, setDetailModal] = useState(null);
  const [products, setProducts] = useState([]);
  const [totalPages, setTotalPages] = useState(1);
  const [totalElements, setTotalElements] = useState(0);
  const [pageSize, setPageSize] = useState(0);
  const [isSearching, setIsSearching] = useState(false); // Check if is searching
  const [isLoading, setIsLoading] = useState(false); // Loading for fetch products
  const [error, setError] = useState(null); // Error response (get, add, edit, delete, get id)
  const [success, setSuccess] = useState(null); // Success response (get, add, edit, delete, get id)
  const [isErrorList, setIsErrorList] = useState(false); // Check if error list (error while fetch products)
  const [disabled, setDisabled] = useState(false); // Set if disable (price from > price to)
  const [highlightedId, setHighlightedId] = useState(null); // Highlight data add/edit

  // Set status true when handling location state
  const locationStateHandled = useRef(false);

  // Validation message
  const validationMessage =
    priceFrom !== "" && priceTo !== "" && Number(priceFrom) > Number(priceTo)
      ? '"From price" cannot be greater than "To price".'
      : (priceFrom !== "" && Number(priceFrom) < 0) ||
          (priceTo !== "" && Number(priceTo) < 0)
        ? "Price cannot be negative."
        : "";

  // List & Search
  const fetchProducts = (currentPage, keyword, priceFromVal, priceToVal) => {
    setIsLoading(true);
    setError(null);

    // Return so that can .then() ... in handle state
    return getProducts({
      page: currentPage,
      keyword: keyword || "",
      priceFrom: priceFromVal || null,
      priceTo: priceToVal || null,
    })
      .then((response) => {
        setIsErrorList(false);
        const { content, totalPages, totalElements, pageSize } =
          response.data.data;
        setProducts(content);
        setTotalPages(totalPages);
        setTotalElements(totalElements);
        setPageSize(pageSize);
        // If 1 of 3 is not empty --> searching
        setIsSearching(
          appliedFilters.search !== "" ||
            appliedFilters.priceFrom !== "" ||
            appliedFilters.priceTo !== "",
        );
        // Promise chain know response
        return response;
      })
      .catch((err) => {
        setError(err.userMessage || "An error occurred");
        setIsErrorList(true);
        setProducts([]);
        setTotalPages(1);
        setTotalElements(0);
        setPageSize(0);
        throw err;
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  // Check if  price from > price to
  // setState
  useEffect(() => {
    const fromNum = Number(priceFrom);
    const toNum = Number(priceTo);

    const invalidRange = priceFrom !== "" && priceTo !== "" && fromNum > toNum;

    const hasNegative =
      (priceFrom !== "" && fromNum < 0) || (priceTo !== "" && toNum < 0);

    const isEmpty = search === "" && priceFrom === "" && priceTo === "";

    setDisabled(invalidRange || hasNegative || isEmpty);
  }, [priceFrom, priceTo, search]);

  // Load for first time, or when list changes (by change page / search)
  // setPage or setAppliedSearch -> fetch products again
  useEffect(() => {
    fetchProducts(
      page,
      appliedFilters.search,
      appliedFilters.priceFrom,
      appliedFilters.priceTo,
    );
  }, [page, appliedFilters]);

  // Handle state from navigation, after add/edit and Toast
  // Change location state, setAppliedSearch, but state.current make sure to run just 1 time
  useEffect(() => {
    if (locationStateHandled.current) return;

    locationStateHandled.current = true;
    const {
      goToLastPage,
      goToPage,
      appliedFilters: returnedFilters,
      type,
      message,
      productId,
    } = location.state || {};
    window.history.replaceState({}, "");

    // Show error / success with toast by message from location.state
    if (type === "error" && message) {
      toast.error(message, {
        position: "top-center",
        autoClose: 2500,
        hideProgressBar: false,
        closeOnClick: false,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light",
        transition: Bounce,
      });
    } else if (type === "success" && message) {
      toast.success(message, {
        position: "top-center",
        autoClose: 2500,
        hideProgressBar: false,
        closeOnClick: false,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light",
        transition: Bounce,
      });
    }

    // Set highlight turnoff after 3s
    if (type === "success" && productId) {
      setHighlightedId(productId);
      setTimeout(() => setHighlightedId(null), 3000);
    }

    const filters =
      returnedFilters !== undefined ? returnedFilters : appliedFilters;

    if (goToLastPage) {
      // Add - fetch to get new total pages, then change to last page
      fetchProducts(1, filters.search, filters.priceFrom, filters.priceTo)
        .then((response) => {
          const newTotalPages = response.data.data.totalPages;
          // Set search & page
          setAppliedFilters(filters);
          setSearch(filters.search);
          setPriceFrom(filters.priceFrom);
          setPriceTo(filters.priceTo);

          if (newTotalPages !== page) {
            setPage(newTotalPages);
          } else {
            // If newTotalPages === page, useEffect not trigger
            // Fetch again
            fetchProducts(
              newTotalPages,
              filters.search,
              filters.priceFrom,
              filters.priceTo,
            );
          }
        })
        .catch(() => {});
    } else if (goToPage) {
      // Edit - fetch then change to page
      fetchProducts(1, filters.search, filters.priceFrom, filters.priceTo)
        .then(() => {
          setAppliedFilters(filters);
          setSearch(filters.search);
          setPriceFrom(filters.priceFrom);
          setPriceTo(filters.priceTo);

          if (goToPage !== page) {
            setPage(goToPage);
          } else {
            fetchProducts(
              goToPage,
              filters.search,
              filters.priceFrom,
              filters.priceTo,
            );
          }
        })
        .catch(() => {});
    } else if (returnedFilters !== undefined) {
      // Just has returnedFilters, but no goToLastPage/goToPage
      setAppliedFilters(filters);
      setSearch(filters.search);
      setPriceFrom(filters.priceFrom);
      setPriceTo(filters.priceTo);
    }
  }, [location.state]);

  // Show error / success with toast from others (delete, etc..)
  useEffect(() => {
    if (error) {
      toast.error(error, {
        position: "top-center",
        autoClose: 2500,
        hideProgressBar: false,
        closeOnClick: false,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light",
        transition: Bounce,
      });
      setError(null);
    } else if (success) {
      toast.success(success, {
        position: "top-center",
        autoClose: 2500,
        hideProgressBar: false,
        closeOnClick: false,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light",
        transition: Bounce,
      });
      setSuccess(null);
    }
  }, [error, success]);

  // Search -> apply search keyword, page = 1
  const handleSearch = () => {
    setAppliedFilters({ search, priceFrom, priceTo });
    setPage(1);
  };

  // Clear search
  const handleClearSearch = () => {
    setSearch("");
    setPriceFrom("");
    setPriceTo("");
    setAppliedFilters({
      search: "",
      priceFrom: "",
      priceTo: "",
    });
    setPage(1);
  };

  // Enter key, not disabled
  const handleKeyDown = (e) => {
    if (e.key === "Enter" && !disabled) handleSearch();
  };

  // Set product to be delete
  const handleDelete = (product) => {
    setDeleteModal(product);
  };

  // Delete product, modal null, page not change or go to previous page
  const confirmDelete = () => {
    if (!deleteModal) return;

    deleteProduct(deleteModal.productId)
      .then((response) => {
        const { responseMsg } = response.data;
        setDeleteModal(null);
        setSuccess(responseMsg);

        // If this page just have 1 element and not the first page
        if (products.length === 1 && page > 1) {
          // page changes -> fetch products
          setPage(page - 1);
        } else {
          fetchProducts(
            page,
            appliedFilters.search,
            appliedFilters.priceFrom,
            appliedFilters.priceTo,
          );
        }
      })
      .catch((err) => {
        setError(err.userMessage || "An error occurred while deleting");
      });
  };

  // Product to be show detail
  const handleShowDetail = (product) => {
    setDetailModal(product);
  };

  // Get image url, if null then set default image
  const getImageUrl = (imagePath) => {
    if (!imagePath) return NoProductImage;
    return `${BASE_URL}/${imagePath}`;
  };

  return (
    <MainLayout pageClassName="products-page" isLoading={isLoading}>
      {/* Page headers */}
      <section className="products-header">
        <div className="products-header-container">
          <div className="products-header-wrapper">
            <h1 className="title">Products Management</h1>
          </div>
        </div>
      </section>

      {/* Actions bar */}
      <section className="products-actions">
        <div className="products-actions-container">
          <div className="products-actions-wrapper">
            {!isLoading && !isErrorList && (
              <>
                <div className="action-groups">
                  {/* Search brand name & product name + Filter: price from, price to */}
                  <div className="search-group">
                    <div className="search-input">
                      <input
                        className="input input-search"
                        type="text"
                        placeholder="Search by product name or brand name…"
                        value={search}
                        onChange={(e) => setSearch(filterInput(e.target.value))}
                        onKeyDown={handleKeyDown}
                      />
                      <i className="bi bi-search search-icon"></i>
                    </div>

                    <div className="price-note-group">
                      {/* Price from */}
                      <div className="price-group">
                        <div className="price-title">From</div>
                        <div className="input-group">
                          <input
                            className="input"
                            type="number"
                            min="0"
                            step="1000"
                            placeholder="From price..."
                            value={priceFrom}
                            onChange={(e) => setPriceFrom(e.target.value)}
                            onKeyDown={handleKeyDown}
                            autoComplete="off"
                          />
                        </div>
                      </div>

                      {/* Price to */}

                      <div className="price-group">
                        <div className="price-title">To</div>
                        <div className="input-group">
                          <input
                            className="input"
                            type="number"
                            min="0"
                            step="1000"
                            placeholder="To price..."
                            value={priceTo}
                            onChange={(e) => setPriceTo(e.target.value)}
                            onKeyDown={handleKeyDown}
                            autoComplete="off"
                          />
                        </div>
                      </div>
                      {validationMessage && (
                        <div className="search-validation">
                          {validationMessage}
                        </div>
                      )}
                    </div>
                    <div className="button-group">
                      <button
                        className="btn btn-search"
                        disabled={disabled}
                        onClick={handleSearch}
                      >
                        Search
                      </button>
                      <button
                        className={`btn btn-clear ${
                          appliedFilters.search === "" &&
                          appliedFilters.priceFrom === "" &&
                          appliedFilters.priceTo === ""
                            ? "disabled"
                            : ""
                        }`}
                        onClick={handleClearSearch}
                      >
                        <i className="bi bi-x"></i> Clear
                      </button>
                    </div>
                  </div>
                  {isAdmin && (
                    <button
                      className="btn btn-primary"
                      onClick={() =>
                        navigate("/products/add", {
                          state: {
                            page: page,
                            appliedFilters: appliedFilters,
                          },
                        })
                      }
                    >
                      Add Product
                    </button>
                  )}
                </div>
              </>
            )}
            {!isLoading && !isErrorList && products.length > 0 && (
              <div className="show-records">
                Showing {pageSize * (page - 1) + 1} to{" "}
                {Math.min(pageSize * page, totalElements)} of {totalElements}{" "}
                {isSearching ? "search " : ""}
                records
              </div>
            )}
          </div>
        </div>
      </section>

      {/* Not found / Table */}
      <section className="products-table">
        <div className="products-table-container">
          <div className="products-table-wrapper">
            {!isLoading && isErrorList ? (
              <p className="not-found">Cannot load products.</p>
            ) : !isLoading && !isErrorList && products.length === 0 ? (
              <p className="not-found">No products found.</p>
            ) : (
              !isLoading &&
              !isErrorList && (
                <>
                  <div className="table-responsive">
                    <table className="table table-striped table-hover">
                      <thead className="table-dark">
                        <tr>
                          <th id="table-header-id">ID</th>
                          <th id="table-header-name">Product Name</th>
                          <th id="table-header-quantity">Quantity</th>
                          <th id="table-header-price">Price</th>
                          <th id="table-header-brand">Brand Name</th>
                          <th id="table-header-image">Image</th>
                          <th id="table-header-description">Description</th>
                          {isAdmin && (
                            <th id="table-header-actions">Actions</th>
                          )}
                        </tr>
                      </thead>
                      <tbody>
                        {products.map((product) => (
                          <tr
                            key={product.productId}
                            className={
                              highlightedId === product.productId
                                ? "row-highlighted"
                                : ""
                            }
                          >
                            <td>
                              <button
                                className="id-link"
                                onClick={() => handleShowDetail(product)}
                              >
                                {product.productId}
                              </button>
                            </td>
                            <td id="table-data-name" className="table-name">
                              <div className="table-clamp">
                                {product.productName}
                              </div>
                            </td>
                            <td id="table-data-quantity">{product.quantity}</td>
                            <td id="table-data-price">
                              {formatPrice(product.price)}
                            </td>
                            <td id="table-data-brand">
                              <div className="table-clamp">
                                {product.brandName}
                              </div>
                            </td>
                            <td id="table-data-image">
                              <div className="table-image">
                                <img
                                  src={getImageUrl(product.image)}
                                  onError={(e) => {
                                    e.target.src = NoProductImage;
                                  }}
                                  alt={product.productName}
                                />
                              </div>
                            </td>
                            <td id="table-data-description">
                              <div className="table-clamp">
                                {product.description}
                              </div>
                            </td>
                            {isAdmin && (
                              <td>
                                <div className="table-actions">
                                  <button
                                    className="btn-action btn-edit"
                                    onClick={() =>
                                      navigate(
                                        `/products/${product.productId}/edit`,
                                        {
                                          state: {
                                            page: page,
                                            appliedFilters: appliedFilters,
                                          },
                                        },
                                      )
                                    }
                                  >
                                    Edit
                                  </button>
                                  <button
                                    className="btn-action btn-delete"
                                    onClick={() => handleDelete(product)}
                                  >
                                    Delete
                                  </button>
                                </div>
                              </td>
                            )}
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>

                  <Pagination
                    totalPages={totalPages}
                    page={page}
                    totalElements={totalElements}
                    pageSize={pageSize}
                    setPage={setPage}
                    isSearching={isSearching}
                  />
                </>
              )
            )}
          </div>
        </div>
      </section>

      {/* Detail model */}
      {detailModal && (
        <div
          className="product-modal-overlay"
          onClick={() => setDetailModal(null)}
        >
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3 className="modal-title">Product Details</h3>
              <button
                className="modal-close"
                onClick={() => setDetailModal(null)}
              >
                &times;
              </button>
            </div>
            <div className="modal-body">
              <div className="detail-row">
                <span className="detail-label">ID:</span>
                <span className="detail-value">{detailModal.productId}</span>
              </div>
              <div className="detail-row">
                <span className="detail-label">Product Name:</span>
                <span className="detail-value">{detailModal.productName}</span>
              </div>
              <div className="detail-row">
                <span className="detail-label">Quantity:</span>
                <span className="detail-value">{detailModal.quantity}</span>
              </div>
              <div className="detail-row">
                <span className="detail-label">Price:</span>
                <span className="detail-value">
                  {formatPrice(detailModal.price)}
                </span>
              </div>
              <div className="detail-row">
                <span className="detail-label">Brand Name:</span>
                <span className="detail-value">{detailModal.brandName}</span>
              </div>
              <div className="detail-row">
                <span className="detail-label">Sale Date:</span>
                <span className="detail-value">
                  {formatDate(detailModal.saleDate)}
                </span>
              </div>
              <div className="detail-row">
                <span className="detail-label">Image:</span>
                <div className="detail-image">
                  <img
                    src={getImageUrl(detailModal.image)}
                    onError={(e) => {
                      e.target.src = NoProductImage;
                    }}
                    alt={detailModal.productName}
                  />
                </div>
              </div>
              <div className="detail-row">
                <span className="detail-label">Description:</span>
                <span className="detail-value">
                  {detailModal.description || "—"}
                </span>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Delete confirmation model */}
      {deleteModal && (
        <div
          className="product-modal-overlay"
          onClick={() => setDeleteModal(null)}
        >
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3 className="modal-title">Confirm Delete</h3>
              <button
                className="modal-close"
                onClick={() => setDeleteModal(null)}
              >
                &times;
              </button>
            </div>
            <div className="modal-body modal-body-delete">
              <p>
                Are you sure you want to delete product{" "}
                <strong>{deleteModal.productName}</strong>?
              </p>
              <p className="modal-warning">This action cannot be undone.</p>
            </div>
            <div className="modal-footer">
              <button
                className="btn btn-secondary"
                onClick={() => setDeleteModal(null)}
              >
                Cancel
              </button>
              <button className="btn btn-danger" onClick={confirmDelete}>
                Delete
              </button>
            </div>
          </div>
        </div>
      )}
    </MainLayout>
  );
}

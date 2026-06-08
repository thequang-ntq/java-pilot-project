import { useState, useEffect, useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import MainLayout from "../../components/layout/MainLayout/MainLayout";
import "./BrandsPage.css";
import Pagination from "../../components/common/Pagination/Pagination";
import { getBrands, deleteBrand } from "../../services/brands-api";
import useAuth from "../../components/context/use-auth";
import NoBrandImage from "../../assets/no-brand-image.jpg";
import { BASE_URL } from "../../utils/constants";
import { filterInput } from "../../utils/utils";
import { toast, Bounce } from "react-toastify";

export default function BrandsPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const { auth } = useAuth();
  const isAdmin = auth?.role === "ADMIN";

  const [search, setSearch] = useState("");
  const [appliedSearch, setAppliedSearch] = useState("");
  const [page, setPage] = useState(1);
  const [deleteModal, setDeleteModal] = useState(null);
  const [detailModal, setDetailModal] = useState(null);
  const [brands, setBrands] = useState([]);
  const [totalPages, setTotalPages] = useState(1);
  const [totalElements, setTotalElements] = useState(0);
  const [pageSize, setPageSize] = useState(0);
  const [isSearching, setIsSearching] = useState(false); // Check if is searching
  const [isLoading, setIsLoading] = useState(false); // Loading for fetch brands
  const [error, setError] = useState(null); // Error response (get, add, edit, delete, get id)
  const [success, setSuccess] = useState(null); // Success response (get, add, edit, delete, get id)
  const [isErrorList, setIsErrorList] = useState(false); // Check if error list (error while fetch brands)
  const [highlightedId, setHighlightedId] = useState(null); // Highlight data add/edit
  const [disabled, setDisabled] = useState(false);

  // Set status true when handling location state
  const locationStateHandled = useRef(false);

  // List & Search
  const fetchBrands = (currentPage, keyword) => {
    setIsLoading(true);
    setError(null);

    // Return so that can .then()... in handle state
    return getBrands({
      page: currentPage,
      keyword: keyword || "",
    })
      .then((response) => {
        setIsErrorList(false);
        const { content, totalPages, totalElements, pageSize } =
          response.data.data;
        setBrands(content);
        setTotalPages(totalPages);
        setTotalElements(totalElements);
        setPageSize(pageSize);
        setIsSearching(appliedSearch !== "");
        // Promise chain know response
        return response;
      })
      .catch((err) => {
        setError(err.userMessage || "An error occurred");
        setIsErrorList(true);
        setBrands([]);
        setTotalPages(1);
        setTotalElements(0);
        setPageSize(0);
        throw err;
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  // If search empty -> disable search button
  useEffect(() => {
    setDisabled(search === "");
  }, [search]);

  // Load for first time, or when list changes (by change page / search)
  // setPage or setAppliedSearch -> fetch brands again
  useEffect(() => {
    fetchBrands(page, appliedSearch);
  }, [page, appliedSearch]);

  // Handle state from navigation, after add/edit
  // Change location state, setAppliedSearch, but state.current make sure to run just 1 time
  // Get search state
  useEffect(() => {
    if (locationStateHandled.current) return;

    locationStateHandled.current = true;
    const {
      goToLastPage,
      goToPage,
      appliedSearch: returnedSearch,
      type,
      message,
      brandId,
    } = location.state || {};
    window.history.replaceState({}, "");

    if (type === "error") setError(message);
    else if (type === "success") setSuccess(message);

    // Set highlight turnoff after 3s
    if (type === "success" && brandId) {
      setHighlightedId(brandId);
      setTimeout(() => setHighlightedId(null), 3000);
    }

    const searchKeyword =
      returnedSearch !== undefined ? returnedSearch : appliedSearch;

    if (goToLastPage) {
      // Add - fetch to get new total pages, then change to last page according to search state
      fetchBrands(1, searchKeyword)
        .then((response) => {
          const newTotalPages = response.data.data.totalPages;
          // Set search & page
          setAppliedSearch(searchKeyword);
          setSearch(searchKeyword);

          if (newTotalPages !== page) {
            setPage(newTotalPages);
          } else {
            // If newTotalPages === page, useEffect not trigger
            // Fetch again
            fetchBrands(newTotalPages, searchKeyword);
          }
        })
        .catch(() => {});
    } else if (goToPage) {
      // Edit - fetch then change to page
      fetchBrands(1, searchKeyword)
        .then(() => {
          setAppliedSearch(searchKeyword);
          setSearch(searchKeyword);
          if (goToPage !== page) {
            setPage(goToPage);
          } else {
            fetchBrands(goToPage, searchKeyword);
          }
        })
        .catch(() => {});
    } else if (returnedSearch !== undefined) {
      // Just has returnedSearch, but no goToLastPage/goToPage
      setAppliedSearch(searchKeyword);
      setSearch(searchKeyword);
    }
  }, [location.state]);

  // Show error / success with toast
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
    setAppliedSearch(search);
    setPage(1);
  };

  // Clear search
  const handleClearSearch = () => {
    setSearch("");
    setAppliedSearch("");
    setPage(1);
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter" && !disabled) handleSearch();
  };

  // Set brand to be delete
  const handleDelete = (brand) => {
    setDeleteModal(brand);
  };

  // Delete brand, modal null, page not change or go to previous page
  const confirmDelete = () => {
    if (!deleteModal) return;

    deleteBrand(deleteModal.brandId)
      .then((response) => {
        const { responseMsg } = response.data;
        setDeleteModal(null);
        setSuccess(responseMsg);

        // If this page just have 1 element and not the first page
        if (brands.length === 1 && page > 1) {
          // page changes -> fetch brands
          setPage(page - 1);
        } else {
          fetchBrands(page, appliedSearch);
        }
      })
      .catch((err) => {
        setError(err.userMessage || "An error occurred while deleting");
      });
  };

  // Brand to show detail
  const handleShowDetail = (brand) => {
    setDetailModal(brand);
  };

  // Get image url, if null then set default image
  const getImageUrl = (imagePath) => {
    if (!imagePath) return NoBrandImage;
    return `${BASE_URL}/${imagePath}`;
  };

  return (
    <MainLayout pageClassName="brands-page" isLoading={isLoading}>
      {/* Page header */}
      <section className="brands-header">
        <div className="brands-header-container">
          <div className="brands-header-wrapper">
            <h1 className="title">Brands Management</h1>
          </div>
        </div>
      </section>

      {/* Actions bar */}
      <section className="brands-actions">
        <div className="brands-actions-container">
          <div className="brands-actions-wrapper">
            {!isLoading && !isErrorList && (
              <>
                <div className="actions">
                  <div className="search-group">
                    <input
                      className="input"
                      type="text"
                      placeholder="Search by brand name…"
                      value={search}
                      onChange={(e) => setSearch(filterInput(e.target.value))}
                      onKeyDown={handleKeyDown}
                    />
                    <i className="bi bi-search search-icon"></i>
                    <button
                      className="btn btn-search"
                      disabled={disabled}
                      onClick={handleSearch}
                    >
                      Search
                    </button>
                    <button
                      className={`btn btn-clear ${!appliedSearch ? "disabled" : ""}`}
                      onClick={handleClearSearch}
                    >
                      <i className="bi bi-x"></i> Clear
                    </button>
                  </div>
                  {isAdmin && (
                    <button
                      className="btn btn-add"
                      onClick={() =>
                        navigate(`/brands/add`, {
                          state: {
                            page: page,
                            appliedSearch: appliedSearch,
                          },
                        })
                      }
                    >
                      Add Brand
                    </button>
                  )}
                </div>
              </>
            )}

            {!isLoading && !isErrorList && brands.length > 0 && (
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
      <section className="brands-table">
        <div className="brands-table-container">
          <div className="brands-table-wrapper">
            {!isLoading && isErrorList ? (
              <p className="not-found">Cannot load brands.</p>
            ) : !isLoading && !isErrorList && brands.length === 0 ? (
              <p className="not-found">No brands found.</p>
            ) : (
              !isLoading &&
              !isErrorList && (
                <>
                  <div className="table-responsive">
                    <table className="table table-striped table-hover">
                      <thead className="table-dark">
                        <tr>
                          <th id="table-header-id">ID</th>
                          <th id="table-header-name">Brand Name</th>
                          <th id="table-header-logo">Logo</th>
                          <th id="table-header-description">Description</th>
                          {isAdmin && (
                            <th id="table-header-actions">Actions</th>
                          )}
                        </tr>
                      </thead>
                      <tbody>
                        {brands.map((brand) => (
                          <tr
                            key={brand.brandId}
                            className={
                              highlightedId === brand.brandId
                                ? "row-highlighted"
                                : ""
                            }
                          >
                            <td>
                              <button
                                className="id-link"
                                onClick={() => handleShowDetail(brand)}
                              >
                                {brand.brandId}
                              </button>
                            </td>
                            <td id="table-data-name" className="table-name">
                              <div className="table-clamp">
                                {brand.brandName}
                              </div>
                            </td>
                            <td id="table-data-logo">
                              <div className="table-logo">
                                <img
                                  src={getImageUrl(brand.logo)}
                                  onError={(e) => {
                                    e.target.src = NoBrandImage;
                                  }}
                                  alt={brand.brandName}
                                />
                              </div>
                            </td>
                            <td
                              id="table-data-description"
                              className="table-description"
                            >
                              <div className="table-clamp">
                                {brand.description}
                              </div>
                            </td>
                            {isAdmin && (
                              <td>
                                <div className="table-actions">
                                  <button
                                    className="btn-action btn-edit"
                                    onClick={() =>
                                      navigate(
                                        `/brands/${brand.brandId}/edit`,
                                        {
                                          state: {
                                            page: page,
                                            appliedSearch: appliedSearch,
                                          },
                                        },
                                      )
                                    }
                                  >
                                    Edit
                                  </button>
                                  <button
                                    className="btn-action btn-delete"
                                    onClick={() => handleDelete(brand)}
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

      {/* Detail modal */}
      {detailModal && (
        <div
          className="brand-modal-overlay"
          onClick={() => setDetailModal(null)}
        >
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3 className="modal-title">Brand Details</h3>
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
                <span className="detail-value">{detailModal.brandId}</span>
              </div>
              <div className="detail-row">
                <span className="detail-label">Brand Name:</span>
                <span className="detail-value">{detailModal.brandName}</span>
              </div>
              <div className="detail-row">
                <span className="detail-label">Logo:</span>
                <div className="detail-logo">
                  <img
                    src={getImageUrl(detailModal.logo)}
                    onError={(e) => {
                      e.target.src = NoBrandImage;
                    }}
                    alt={detailModal.brandName}
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

      {/* Delete confirmation modal */}
      {deleteModal && (
        <div
          className="brand-modal-overlay"
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
                Are you sure you want to delete brand{" "}
                <strong>{deleteModal.brandName}</strong>?
              </p>
              <p className="modal-warning">
                This will also delete all products in this brand.
              </p>
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

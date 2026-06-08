import { useState, useEffect, useRef } from "react";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import MainLayout from "../../components/layout/MainLayout/MainLayout";
import "./ProductFormPage.css";
import {
  getProductById,
  addProduct,
  updateProduct,
} from "../../services/products-api";
import { getBrands } from "../../services/brands-api";
import { sanitize, filterInput } from "../../utils/utils";
import NoProductImage from "../../assets/no-product-image.jpeg";
import {
  BASE_URL,
  MAX_FILE_SIZE_MB,
  MAX_FILE_SIZE_BYTES,
  ALLOWED_TYPES,
} from "../../utils/constants";

export default function ProductFormPage() {
  const { id } = useParams(); // Get ID from URL
  const navigate = useNavigate(); // Navigate
  const location = useLocation(); // Location
  const isEdit = Boolean(id); // is edit ?
  const page = location.state?.page || 1; // Get pages from ProductsPage state that has edit element
  const appliedFilters = location.state?.appliedFilters || {
    search: "",
    priceFrom: "",
    priceTo: "",
  };

  const [name, setName] = useState("");
  const [quantity, setQuantity] = useState("");
  const [price, setPrice] = useState("");
  const [brandId, setBrandId] = useState("");
  const [saleDate, setSaleDate] = useState("");
  const [description, setDescription] = useState("");
  const [imageFile, setImageFile] = useState(null);
  const [imagePreview, setImagePreview] = useState("");
  const [brands, setBrands] = useState([]); // List brands to show
  const [allBrands, setAllBrands] = useState([]); // Save all brands
  const [brandSearch, setBrandSearch] = useState(""); // Search brand input
  const [showBrandDropdown, setShowBrandDropdown] = useState(false); // Dropdown for Brand field
  const brandDropdownRef = useRef(null); // Reference to Brand field dropdown
  const [errors, setErrors] = useState({}); // Error response
  const [isLoading, setIsLoading] = useState(false); // Loading spinner
  const [isImageDeleted, setIsImageDeleted] = useState(false);
  const [imageFileName, setImageFileName] = useState("");

  // Clear state from ProductsPage
  useEffect(() => {
    if (location.state?.page) {
      window.history.replaceState({}, "");
    }
  }, [location]);

  // Click outside to close dropdown
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        brandDropdownRef.current &&
        !brandDropdownRef.current.contains(event.target)
      ) {
        setShowBrandDropdown(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  // Fetch all brands & get top 10 brands first satisfy the condition (not find yet) to choose brand names
  useEffect(() => {
    setIsLoading(true);
    getBrands({ page: 1, keyword: "" })
      .then((response) => {
        const allBrandsList = [];
        const totalPages = response.data.data.totalPages;

        // Call API for each page, fetchPromises contains Promises wait for API to complete
        // Sort by brand name
        // Ex: [Promise<[10 brands from page i]>, ...]
        // TODO: Fetch all brands from DB -> Dangerous
        const fetchPromises = [];
        for (let i = 1; i <= totalPages; i++) {
          fetchPromises.push(
            getBrands({ page: i, keyword: "", isSortByName: true }).then(
              (res) => res.data.data.content,
            ),
          );
        }

        // After promise.all, results contains real data after all requests completed
        // 1 elements in results is a list brands of that page
        // allBrands merge all list brands into just brands in 1 array
        Promise.all(fetchPromises).then((results) => {
          results.forEach((content) => allBrandsList.push(...content));
          setAllBrands(allBrandsList);

          // Top 10 brands
          setBrands(allBrandsList.slice(0, 10));
        });
      })
      .finally(() => {
        setIsLoading(false);
      });
  }, []);

  // Has id -> get data and set to input fields (Edit has old data)
  // Else come back to page has row need edit in products page
  useEffect(() => {
    if (isEdit) {
      setIsLoading(true);
      getProductById(id)
        .then((response) => {
          const product = response.data.data;
          setName(product.productName);
          setQuantity(product.quantity.toString());
          setPrice(product.price.toString());
          setBrandId(product.brandId.toString());
          setBrandSearch(product.brandName); // Set search input been search is old brand name
          setSaleDate(product.saleDate);
          setDescription(product.description || "");
          setImagePreview(product.image ? `${BASE_URL}/${product.image}` : "");
          // Set file name
          if (product.image) {
            const fileName = product.image.split("/").pop();
            setImageFileName(fileName);
          }
        })
        .catch((err) => {
          navigate("/products", {
            state: {
              goToPage: page,
              appliedFilters: appliedFilters,
              type: "error",
              message: err.userMessage || "Error when getting product",
            },
            replace: true,
          });
        })
        .finally(() => {
          setIsLoading(false);
        });
    }
  }, [id, isEdit, navigate, page]);

  // Filter branch by search
  useEffect(() => {
    // If not search in brand field -> Load default
    if (brandSearch.trim() === "") {
      setBrands(allBrands.slice(0, 10));
    } else {
      // Filter all brands satisfy the condition (name)
      const filtered = allBrands.filter((brand) =>
        brand.brandName.toLowerCase().includes(brandSearch.toLowerCase()),
      );
      // Get top 10 brands
      setBrands(filtered.slice(0, 10));
    }
  }, [brandSearch, allBrands]);

  // Handle brand search input change
  const handleBrandSearchChange = (e) => {
    const value = filterInput(e.target.value);
    setBrandSearch(value);
    setBrandId(""); // Reset brandId while searching
    setShowBrandDropdown(true);
    // clearError("brandId");
  };

  // Handle choose brand from dropdown
  const handleSelectBrand = (brand) => {
    setBrandId(brand.brandId.toString());
    setBrandSearch(brand.brandName);
    setShowBrandDropdown(false);
    // clearError("brandId");
  };

  // Validate for submit
  const validate = () => {
    const errs = {};
    if (!name.trim()) errs.name = "Product name is required.";
    if (!quantity.trim()) errs.quantity = "Quantity is required.";
    else if (parseInt(quantity) < 0)
      errs.quantity = "Quantity must be at least 0.";
    if (!price.trim()) errs.price = "Price is required.";
    else if (parseFloat(price) < 0) errs.price = "Price must be at least 0.";
    if (!brandId) errs.brandId = "Brand is required.";
    if (!saleDate) errs.saleDate = "Sale date is required.";
    else if (saleDate > new Date().toISOString().split("T")[0])
      errs.saleDate = "Sale date cannot be in the future";
    return errs;
  };

  // Change product image
  const handleImageChange = (e) => {
    const file = e.target.files?.[0];
    if (file) {
      if (!ALLOWED_TYPES.includes(file.type)) {
        setErrors((prev) => ({
          ...prev,
          image: "Only PNG, JPG, JPEG, WEBP, GIF files are allowed.",
        }));
        return;
      }
      if (file.size > MAX_FILE_SIZE_BYTES) {
        setErrors((prev) => ({
          ...prev,
          image: `Image size must not exceed ${MAX_FILE_SIZE_MB}MB.`,
        }));
        return;
      }
      setImageFile(file);
      setImagePreview(URL.createObjectURL(file));
      setImageFileName(file.name);
      setIsImageDeleted(false);
      setErrors((prev) => ({ ...prev, image: "" }));
    }
  };

  // Submit and save the value
  const handleSubmit = () => {
    const errs = validate();
    if (Object.keys(errs).length > 0) {
      setErrors(errs);
      return;
    }
    setErrors({});

    // If has image then save its url,
    const formData = new FormData();
    formData.append("productName", sanitize(name));
    formData.append("quantity", parseInt(quantity));
    formData.append("price", parseFloat(price));
    formData.append("brandId", parseInt(brandId));
    formData.append("saleDate", saleDate);
    formData.append("description", sanitize(description));

    // If has imageFile then save its url
    if (imageFile) {
      formData.append("imageFiles", imageFile);
    }

    // If not -> null
    if (isEdit && isImageDeleted && !imageFile) {
      formData.append("deleteImage", "true"); //Flag for deleteOldFile
    }

    setIsLoading(true);
    const apiCall = isEdit ? updateProduct(id, formData) : addProduct(formData);

    //Parse state goToPage = page when edit return to list, and goToLastPage: true when add return to list
    apiCall
      .then((response) => {
        const { responseMsg, data } = response.data;
        navigate("/products", {
          state: {
            [isEdit ? "goToPage" : "goToLastPage"]: isEdit ? page : true,
            appliedFilters: appliedFilters,
            type: "success",
            message: responseMsg,
            productId: isEdit ? parseInt(id) : data?.productId,
          },
          replace: true,
        });
      })
      .catch((err) => {
        // Error display in this page
        if (err.response?.data?.responseCode === 409) {
          setErrors({ name: "Product name already exists." });
        } else {
          // Error display in products page
          navigate("/products", {
            state: {
              [isEdit ? "goToPage" : "goToLastPage"]: isEdit ? page : true,
              appliedFilters: appliedFilters,
              type: "error",
              message: err.userMessage || "An error occurred",
            },
            replace: true,
          });
        }
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  // Update state object, delete 1 error field for required fields
  const clearError = (field) => {
    setErrors((prev) => ({ ...prev, [field]: "" }));
  };

  return (
    <MainLayout pageClassName="product-form-page" isLoading={isLoading}>
      <section className="product-form-section">
        <div className="product-form-container">
          <div className="product-form-wrapper">
            {/* Header */}
            <div className="form-header">
              <h1 className="title">
                {isEdit ? "Edit Product" : "Add New Product"}
              </h1>
            </div>

            {/* Form */}
            <div className="form">
              <div className="form-fields">
                <div className="left-column">
                  {/* Name */}
                  <div className="field">
                    <label className="field-label" htmlFor="product-name">
                      Product Name <span className="required">*</span>
                    </label>
                    <div className="field-input-group">
                      <input
                        id="product-name"
                        className={`field-input ${errors.name ? "field-input-error" : ""}`}
                        type="text"
                        placeholder="Enter product name"
                        value={name}
                        onChange={(e) => {
                          setName(filterInput(e.target.value));
                        }}
                        onFocus={() => {
                          clearError("name");
                        }}
                        maxLength={50}
                        autoComplete="off"
                      />
                      {errors.name && (
                        <span className="field-error">{errors.name}</span>
                      )}
                    </div>
                  </div>

                  {/* Quantity */}
                  <div className="field">
                    <label className="field-label" htmlFor="product-quantity">
                      Quantity <span className="required">*</span>
                    </label>
                    <div className="field-input-group">
                      <input
                        id="product-quantity"
                        className={`field-input ${errors.quantity ? "field-input-error" : ""}`}
                        type="number"
                        min="0"
                        placeholder="Enter quantity"
                        value={quantity}
                        onChange={(e) => {
                          setQuantity(e.target.value);
                        }}
                        onFocus={() => {
                          clearError("quantity");
                        }}
                      />
                      {errors.quantity && (
                        <span className="field-error">{errors.quantity}</span>
                      )}
                    </div>
                  </div>

                  {/* Price */}
                  <div className="field">
                    <label className="field-label" htmlFor="product-price">
                      Price <span className="required">*</span>
                    </label>
                    <div className="field-input-group">
                      <input
                        id="product-price"
                        className={`field-input ${errors.price ? "field-input-error" : ""}`}
                        type="number"
                        min="0"
                        step="1000"
                        placeholder="Enter price"
                        value={price}
                        onChange={(e) => {
                          setPrice(e.target.value);
                        }}
                        onFocus={() => {
                          clearError("price");
                        }}
                      />
                      {errors.price && (
                        <span className="field-error">{errors.price}</span>
                      )}
                    </div>
                  </div>

                  {/* List brands */}
                  <div className="field" ref={brandDropdownRef}>
                    <label className="field-label" htmlFor="product-brand">
                      Brand <span className="required">*</span>
                    </label>
                    <div className="field-input-group">
                      <div className="field-input-wrapper">
                        {/* Search and select a brand name, but value is brand id */}
                        <input
                          id="product-brand"
                          className={`field-input ${errors.brandId ? "field-input-error" : ""}`}
                          type="text"
                          placeholder="Search and select a brand"
                          value={brandSearch}
                          onChange={handleBrandSearchChange}
                          onFocus={() => {
                            setShowBrandDropdown(true);
                            clearError("brandId");
                          }}
                          maxLength={50}
                          autoComplete="off"
                        />
                        {/* Selection arrow */}
                        <button
                          className={`arrow-button ${showBrandDropdown ? "arrow-active" : ""}`}
                          onClick={() => {
                            setShowBrandDropdown(true);
                            clearError("brandId");
                          }}
                        >
                          <i className="bi bi-caret-down-fill arrow-icon"></i>
                        </button>

                        {/* Brand dropdown */}
                        {showBrandDropdown && brands.length > 0 && (
                          <div className="brand-dropdown">
                            {brands.map((brand) => (
                              <div
                                key={brand.brandId}
                                className="brand-dropdown-item"
                                onClick={() => handleSelectBrand(brand)}
                              >
                                {brand.brandName}
                              </div>
                            ))}
                          </div>
                        )}
                      </div>
                      {/* Error of field */}
                      {errors.brandId && (
                        <span className="field-error">{errors.brandId}</span>
                      )}
                    </div>
                  </div>

                  {/* Sale date */}
                  <div className="field">
                    <label className="field-label" htmlFor="product-sale-date">
                      Sale Date <span className="required">*</span>
                    </label>
                    <div className="field-input-group">
                      <input
                        id="product-sale-date"
                        className={`
                          field-input
                          ${!saleDate ? "field-date-placeholder" : ""}
                          ${errors.saleDate ? "field-input-error" : ""}
                        `}
                        type="date"
                        value={saleDate}
                        onChange={(e) => {
                          setSaleDate(e.target.value);
                        }}
                        onFocus={() => {
                          clearError("saleDate");
                        }}
                      />
                      {errors.saleDate && (
                        <span className="field-error">{errors.saleDate}</span>
                      )}
                    </div>
                  </div>
                </div>
                <div className="right-column">
                  {/* Image */}
                  <div className="field">
                    <label className="field-label">Image</label>
                    <div className="field-input-group image-input-group">
                      <div className="choose-file-group">
                        <label
                          htmlFor="product-image"
                          className="custom-file-btn"
                        >
                          Choose Image
                        </label>
                        {/* Show file name */}
                        {imageFileName && (
                          <div className="file-name">{imageFileName}</div>
                        )}
                      </div>
                      <input
                        id="product-image"
                        className="field-file"
                        type="file"
                        accept=".png,.jpg,.jpeg,.webp,.gif"
                        onChange={handleImageChange}
                        onClick={(e) => {
                          e.target.value = null;
                          setErrors((prev) => ({ ...prev, image: "" }));
                        }}
                      />
                      {/* File types */}
                      <span className="file-types">
                        PNG, JPG, JPEG, WEBP, GIF • Max {MAX_FILE_SIZE_MB}MB
                      </span>

                      {/* Blank */}
                      {!errors.image && <div className="spacer"></div>}

                      {/* Show error */}
                      {errors.image && (
                        <span className="field-error">{errors.image}</span>
                      )}

                      {/* Preview logo */}
                      <div className="field-preview">
                        {imagePreview ? (
                          <>
                            <img
                              src={imagePreview}
                              onError={(e) => {
                                e.target.src = NoProductImage;
                              }}
                              alt="Product preview"
                            />
                            <button
                              type="button"
                              className="field-preview-remove"
                              onClick={() => {
                                setImagePreview("");
                                setImageFile(null);
                                setImageFileName("");
                                setIsImageDeleted(true);
                                setErrors((prev) => ({ ...prev, image: "" }));
                                // Reset input file
                                document.getElementById("product-image").value =
                                  "";
                              }}
                              title="Remove logo"
                            >
                              ✕
                            </button>
                          </>
                        ) : (
                          <div className="thumbnail">No image yet</div>
                        )}
                      </div>
                    </div>
                  </div>

                  {/* Description  */}
                  <div className="field">
                    <label
                      className="field-label"
                      htmlFor="product-description"
                    >
                      Description
                    </label>
                    <div className="field-input-group">
                      <textarea
                        id="product-description"
                        className="field-textarea"
                        placeholder="Enter product description"
                        value={description}
                        onChange={(e) => {
                          setDescription(filterInput(e.target.value));
                        }}
                        maxLength={255}
                        rows={4}
                      />
                    </div>
                  </div>
                </div>
              </div>

              <div className="form-divider"></div>

              {/* Action buttons */}
              <div className="form-actions">
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() =>
                    navigate("/products", {
                      state: {
                        goToPage: page,
                        appliedFilters: appliedFilters,
                      },
                    })
                  }
                >
                  Back
                </button>
                <button
                  type="button"
                  className="btn btn-primary"
                  onClick={handleSubmit}
                >
                  {isEdit ? "Save" : "Add"}
                </button>
              </div>
            </div>
          </div>
        </div>
      </section>
    </MainLayout>
  );
}

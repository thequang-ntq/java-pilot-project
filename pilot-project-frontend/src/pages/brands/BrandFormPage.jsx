import { useState, useEffect } from "react";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import MainLayout from "../../components/layout/MainLayout/MainLayout";
import "./BrandFormPage.css";
import { getBrandById, addBrand, updateBrand } from "../../services/brands-api";
import { sanitize, filterInput } from "../../utils/utils";
import NoBrandImage from "../../assets/no-brand-image.jpg";
import {
  BASE_URL,
  MAX_FILE_SIZE_MB,
  MAX_FILE_SIZE_BYTES,
  ALLOWED_TYPES,
} from "../../utils/constants";

export default function BrandFormPage() {
  const { id } = useParams(); // Get id from URL
  const navigate = useNavigate(); // Navigate
  const location = useLocation(); // Location
  const isEdit = Boolean(id); // Has id -> edit
  const page = location.state?.page || 1; // Get page from BrandsPage state that has edit element
  const appliedSearch = location.state?.appliedSearch || ""; // Search state

  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [logoFile, setLogoFile] = useState(null);
  const [logoPreview, setLogoPreview] = useState("");
  const [errors, setErrors] = useState({}); // Error response
  const [isLoading, setIsLoading] = useState(false); // loading spinner
  const [isLogoDeleted, setIsLogoDeleted] = useState(false); // Check if logo deleted using X button on logo
  const [logoFileName, setLogoFileName] = useState(""); // File name

  // CLear state from BrandsPage
  useEffect(() => {
    if (location.state?.page || location.state?.appliedSearch) {
      window.history.replaceState({}, "");
    }
  }, [location]);

  // Has id -> get data and set to input fields
  // Else comeback to page has row need edit in brands page
  useEffect(() => {
    if (isEdit) {
      setIsLoading(true);
      getBrandById(id)
        .then((response) => {
          const brand = response.data.data;
          setName(brand.brandName);
          setDescription(brand.description || "");
          setLogoPreview(brand.logo ? `${BASE_URL}/${brand.logo}` : "");
          // Set file name
          if (brand.logo) {
            const fileName = brand.logo.split("/").pop();
            setLogoFileName(fileName);
          }
        })
        .catch((err) => {
          navigate("/brands", {
            state: {
              goToPage: page,
              appliedSearch: appliedSearch,
              type: "error",
              message: err.userMessage || "Error when getting brand",
            },
            replace: true,
          });
        })
        .finally(() => {
          setIsLoading(false);
        });
    }
  }, [id, isEdit, navigate, page, appliedSearch]);

  // Check if data valid
  const validate = () => {
    const errs = {};
    if (!name.trim()) {
      errs.name = "Brand name is required.";
    }
    return errs;
  };

  // Change logo image
  const handleLogoChange = (e) => {
    const file = e.target.files?.[0];
    if (file) {
      if (!ALLOWED_TYPES.includes(file.type)) {
        setErrors((prev) => ({
          ...prev,
          logo: "Only PNG, JPG, JPEG, WEBP, GIF files are allowed.",
        }));
        return;
      }
      if (file.size > MAX_FILE_SIZE_BYTES) {
        setErrors((prev) => ({
          ...prev,
          logo: `Image size must not exceed ${MAX_FILE_SIZE_MB}MB.`,
        }));
        return;
      }
      setLogoFile(file);
      setLogoPreview(URL.createObjectURL(file));
      setLogoFileName(file.name);
      setIsLogoDeleted(false);
      setErrors((prev) => ({ ...prev, logo: "" }));
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

    const formData = new FormData();
    formData.append("brandName", sanitize(name));
    formData.append("description", sanitize(description));

    // If has logoFile then save its url
    if (logoFile) {
      formData.append("logoFiles", logoFile);
    }

    // If not -> null
    if (isEdit && isLogoDeleted && !logoFile) {
      formData.append("deleteLogo", "true"); //Flag for deleteOldFile
    }

    setIsLoading(true);
    const apiCall = isEdit ? updateBrand(id, formData) : addBrand(formData);

    //Parse state goToPage = page when edit return to list, and goToLastPage: true when add return to list
    apiCall
      .then((response) => {
        const { responseMsg, data } = response.data;
        navigate("/brands", {
          state: {
            [isEdit ? "goToPage" : "goToLastPage"]: isEdit ? page : true,
            appliedSearch: appliedSearch,
            type: "success",
            message: responseMsg,
            brandId: isEdit ? parseInt(id) : data?.brandId,
          },
          replace: true,
        });
      })
      .catch((err) => {
        // Error display in this page
        if (err.response?.data?.responseCode === 409) {
          setErrors({ name: "Brand name already exists." });
        } else {
          // Error display in brands page
          navigate("/brands", {
            state: {
              [isEdit ? "goToPage" : "goToLastPage"]: isEdit ? page : true,
              appliedSearch: appliedSearch,
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
    <MainLayout pageClassName="brand-form-page" isLoading={isLoading}>
      <section className="brand-form-section">
        <div className="brand-form-container">
          <div className="brand-form-wrapper">
            {/* Header */}
            <div className="form-header">
              <h1 className="title">
                {isEdit ? "Edit Brand" : "Add New Brand"}
              </h1>
            </div>

            {/* Form */}
            <div className="form">
              <div className="form-fields">
                {/* Name */}
                <div className="field">
                  <label className="field-label" htmlFor="brand-name">
                    Brand Name <span className="required">*</span>
                  </label>
                  <div className="field-input-group">
                    <input
                      id="brand-name"
                      className={`field-input ${errors.name ? "field-input-error" : ""}`}
                      type="text"
                      placeholder="Enter brand name"
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

                {/* Logo */}
                <div className="field">
                  <label className="field-label">Logo</label>
                  <div className="field-input-group logo-input-group">
                    <div className="choose-file-group">
                      <label htmlFor="brand-logo" className="custom-file-btn">
                        Choose Image
                      </label>
                      {/* Show file name */}
                      {logoFileName && (
                        <div className="file-name">{logoFileName}</div>
                      )}
                    </div>
                    <input
                      id="brand-logo"
                      className="field-file"
                      type="file"
                      accept=".png,.jpg,.jpeg,.webp,.gif"
                      onChange={handleLogoChange}
                      onClick={(e) => {
                        e.target.value = null;
                        setErrors((prev) => ({ ...prev, logo: "" }));
                      }}
                    />
                    {/* File types */}
                    <span className="file-types">
                      PNG, JPG, JPEG, WEBP, GIF • Max {MAX_FILE_SIZE_MB}MB
                    </span>

                    {/* Blank */}
                    {!errors.logo && <div className="spacer"></div>}
                    {/* Show error */}
                    {errors.logo && (
                      <span className="field-error">{errors.logo}</span>
                    )}

                    {/* Preview logo */}
                    <div className="field-preview">
                      {logoPreview ? (
                        <>
                          <img
                            src={logoPreview}
                            onError={(e) => {
                              e.target.src = NoBrandImage;
                            }}
                            alt="Logo preview"
                          />
                          <button
                            type="button"
                            className="field-preview-remove"
                            onClick={() => {
                              setLogoPreview("");
                              setLogoFile(null);
                              setLogoFileName("");
                              setIsLogoDeleted(true);
                              setErrors((prev) => ({ ...prev, logo: "" }));
                              // Reset input file
                              document.getElementById("brand-logo").value = "";
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

                {/* Description */}
                <div className="field">
                  <label className="field-label" htmlFor="brand-description">
                    Description
                  </label>
                  <div className="field-input-group">
                    <textarea
                      id="brand-description"
                      className="field-textarea"
                      placeholder="Enter brand description"
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

              <div className="form-divider"></div>

              {/* Actions */}
              <div className="form-actions">
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() =>
                    navigate("/brands", {
                      state: {
                        goToPage: page,
                        appliedSearch: appliedSearch,
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

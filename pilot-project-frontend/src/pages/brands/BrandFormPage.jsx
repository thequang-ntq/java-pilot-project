import { useState, useEffect } from "react";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import MainLayout from "../../components/layout/MainLayout/MainLayout";
import "./BrandFormPage.css";
import {
  getBrandById,
  addBrand,
  updateBrand,
  isBrandNameTaken,
} from "../../data/mock-data";
import { sanitize, filterInput, saveImageFile } from "../../utils/utils";
// import { NoBrandImage } from "../../../assets/no-brand-image.jpg";

export default function BrandFormPage() {
  const { id } = useParams(); // Get id from URL
  const navigate = useNavigate(); // Navigate
  const location = useLocation(); // Location
  const isEdit = Boolean(id); // Has id -> edit
  const page = location.state?.page || 1; // Get page from AdminBrandsPage state that has edit element

  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [logoFile, setLogoFile] = useState(null);
  const [logoPreview, setLogoPreview] = useState("");
  const [errors, setErrors] = useState({});
  const [submitError, setSubmitError] = useState("");

  // CLear state from AdminsBrandPage
  useEffect(() => {
    if (location.state?.page) {
      window.history.replaceState({}, "");
    }
  }, [location]);

  // Has id -> get data and set to input fields
  useEffect(() => {
    if (isEdit) {
      const brand = getBrandById(id);
      if (!brand || brand.isDeleted) {
        navigate("/admin/brands", { replace: true });
        return;
      }
      setName(brand.name);
      setDescription(brand.description || "");
      setLogoPreview(brand.logo);
    }
  }, [id, isEdit, navigate]);

  // Check if data valid
  const validate = () => {
    const errs = {};
    const trimmedName = name.trim();

    if (!trimmedName) {
      errs.name = "Brand name is required.";
    } else if (isBrandNameTaken(trimmedName, id)) {
      errs.name = "Brand name already exists.";
    }

    return errs;
  };

  // Change logo image
  const handleLogoChange = (e) => {
    const file = e.target.files?.[0];
    if (file) {
      if (!file.type.startsWith("image/")) {
        setErrors((prev) => ({
          ...prev,
          logo: "Please select an image file.",
        }));
        return;
      }
      setLogoFile(file);
      setLogoPreview(URL.createObjectURL(file));
      setErrors((prev) => ({ ...prev, logo: "" }));
    }
  };

  // Reset value
  const handleReset = () => {
    if (isEdit) {
      const brand = getBrandById(id);
      if (brand) {
        setName(brand.name);
        setDescription(brand.description || "");
        setLogoPreview(brand.logo);
        setLogoFile(null);
      }
    } else {
      setName("");
      setDescription("");
      setLogoPreview("");
      setLogoFile(null);
    }
    setErrors({});
    setSubmitError("");
  };

  // Submit and save the value
  const handleSubmit = () => {
    setSubmitError("");
    const errs = validate();
    if (Object.keys(errs).length > 0) {
      setErrors(errs);
      return;
    }
    setErrors({});

    // If has logoFile then save its url, otherwise check if has logoPreview then take it, otherwise get noB
    const formatted = {
      name: sanitize(name),
      description: sanitize(description),
      logo: logoFile ? saveImageFile(logoFile) : logoPreview,
    };

    //Parse state goToPage = page when edit return to list, and goToLastPage: true when add return to list
    try {
      if (isEdit) {
        updateBrand(id, formatted);
        navigate("/admin/brands", {
          state: { goToPage: page },
          replace: true,
        });
      } else {
        addBrand(formatted);
        navigate("/admin/brands", {
          state: { goToLastPage: true },
          replace: true,
        });
      }
    } catch (error) {
      setSubmitError("An error occurred: " + error + ". Please try again.");
    }
  };

  const clearError = (field) => {
    setErrors((prev) => ({ ...prev, [field]: "" }));
    setSubmitError("");
  };

  return (
    <MainLayout pageClassName="add-edit-brand-page">
      <section className="add-edit-brand-section">
        <div className="add-edit-brand-container">
          <div className="add-edit-brand-wrapper">
            {/* Header */}
            <div className="form-header">
              <h1 className="title">
                {isEdit ? "Edit Brand" : "Add New Brand"}
              </h1>
            </div>

            {/* Submit error */}
            {submitError && (
              <div className="form-alert form-alert-error">{submitError}</div>
            )}

            {/* Form */}
            <div className="form">
              {/* Name */}
              <div className="field">
                <label className="field-label" htmlFor="brand-name">
                  Brand Name <span className="required">*</span>
                </label>
                <input
                  id="brand-name"
                  className={`field-input ${errors.name ? "field-input-error" : ""}`}
                  type="text"
                  placeholder="Enter brand name"
                  value={name}
                  onChange={(e) => {
                    setName(filterInput(e.target.value));
                    clearError("name");
                  }}
                  autoComplete="off"
                />
                {errors.name && (
                  <span className="field-error">{errors.name}</span>
                )}
              </div>

              {/* Logo */}
              <div className="field">
                <label className="field-label" htmlFor="brand-logo">
                  Logo
                </label>
                <input
                  id="brand-logo"
                  className="field-file"
                  type="file"
                  accept="image/*"
                  onChange={handleLogoChange}
                />
                {errors.logo && (
                  <span className="field-error">{errors.logo}</span>
                )}
                {logoPreview && (
                  <div className="field-preview">
                    <img src={logoPreview} alt="Logo preview" />
                  </div>
                )}
              </div>

              {/* Description */}
              <div className="field">
                <label className="field-label" htmlFor="brand-description">
                  Description
                </label>
                <textarea
                  id="brand-description"
                  className="field-textarea"
                  placeholder="Enter brand description"
                  value={description}
                  onChange={(e) => {
                    setDescription(filterInput(e.target.value));
                  }}
                  rows={4}
                />
              </div>

              {/* Actions */}
              <div className="form-actions">
                <button
                  type="button"
                  className="btn btn-reset"
                  onClick={handleReset}
                >
                  Reset
                </button>
                <button
                  type="button"
                  className="btn btn-primary"
                  onClick={handleSubmit}
                >
                  {isEdit ? "Save" : "Add"}
                </button>
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() => navigate("/admin/brands")}
                >
                  Go Back
                </button>
              </div>
            </div>
          </div>
        </div>
      </section>
    </MainLayout>
  );
}

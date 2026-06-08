import { useState, useEffect, useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import MainLayout from "../../components/layout/MainLayout/MainLayout";
import "./BrandsPage.css";
import Pagination from "../../components/common/Pagination/Pagination";
import {
  getBrands,
  softDeleteBrand,
  countProductsInBrand,
} from "../../data/mock-data";

const PAGE_SIZE = 10;

export default function BrandsPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const [search, setSearch] = useState("");
  const [appliedSearch, setAppliedSearch] = useState("");
  const [page, setPage] = useState(1);
  const [deleteModal, setDeleteModal] = useState(null);
  const [detailModal, setDetailModal] = useState(null);

  // Get list of brands
  const brands = getBrands();

  // Get list of brands has name include the search keyword applied
  const filtered = brands.filter((b) =>
    b.name.toLowerCase().includes(appliedSearch.toLowerCase()),
  );

  // Total page
  const totalPages = Math.max(1, Math.ceil(filtered.length / PAGE_SIZE));

  // Get list of brands paginated
  const paged = filtered.slice((page - 1) * PAGE_SIZE, page * PAGE_SIZE);

  // Handle state and default case when not have state
  const locationStateHandled = useRef(false);
  useEffect(() => {
    if (locationStateHandled.current) return;

    locationStateHandled.current = true;
    const { goToLastPage, goToPage } = location.state || {};
    window.history.replaceState({}, ""); // Clear state

    if (goToLastPage) {
      setPage(totalPages);
    } else if (goToPage) {
      setPage(goToPage);
    }
  }, [location.state, totalPages]);

  // Search -> apply search keyword, page = 1
  const handleSearch = () => {
    setAppliedSearch(search);
    setPage(1);
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter") handleSearch();
  };

  // Set brand to be delete
  const handleDelete = (brand) => {
    setDeleteModal(brand);
  };

  // Soft delete brand, modal null, page = 1
  const confirmDelete = () => {
    if (deleteModal) {
      softDeleteBrand(deleteModal.id);
      setDeleteModal(null);
      if (paged.length === 1 && page > 1) {
        setPage(page - 1);
      }
    }
  };

  // Brand to show detail
  const handleShowDetail = (brand) => {
    setDetailModal(brand);
  };

  return (
    <MainLayout pageClassName="brands-page">
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
            <div className="search-group">
              <input
                className="input"
                type="text"
                placeholder="Search by brand name…"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                onKeyDown={handleKeyDown}
              />
              <button className="btn btn-search" onClick={handleSearch}>
                Search
              </button>
            </div>
            <button
              className="btn btn-primary"
              onClick={() => navigate("/admin/brands/add")}
            >
              Add new brand
            </button>
          </div>
        </div>
      </section>

      {/* Table */}
      <section className="brands-table">
        <div className="brands-table-container">
          <div className="brands-table-wrapper">
            {paged.length === 0 ? (
              <p className="not-found">No brands found.</p>
            ) : (
              <>
                <div className="table-responsive">
                  <table className="table table-striped">
                    <thead className="table-dark">
                      <tr>
                        <th>ID</th>
                        <th>Brand Name</th>
                        <th>Logo</th>
                        <th>Status</th>
                        <th>Actions</th>
                      </tr>
                    </thead>
                    <tbody>
                      {paged.map((brand) => (
                        <tr key={brand.id}>
                          <td>
                            <button
                              className="id-link"
                              onClick={() => handleShowDetail(brand)}
                            >
                              {brand.id}
                            </button>
                          </td>
                          <td className="table-name">{brand.name}</td>
                          <td>
                            <div className="table-logo">
                              <img src={brand.logo} alt={brand.name} />
                            </div>
                          </td>
                          <td>
                            <span
                              className={`status ${brand.isDeleted ? "status-delete" : "status-active"}`}
                            >
                              {brand.isDeleted ? "Deleted" : "Active"}
                            </span>
                          </td>
                          <td>
                            {!brand.isDeleted && (
                              <div className="table-actions">
                                <button
                                  className="btn-action btn-edit"
                                  onClick={() =>
                                    navigate(`/admin/brands/${brand.id}/edit`, {
                                      state: { page: page },
                                    })
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
                            )}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>

                <Pagination
                  totalPages={totalPages}
                  page={page}
                  setPage={setPage}
                />
              </>
            )}
          </div>
        </div>
      </section>

      {/* Detail modal */}
      {detailModal && (
        <div className="modal-overlay" onClick={() => setDetailModal(null)}>
          <div
            className="modal modal-detail"
            onClick={(e) => e.stopPropagation()}
          >
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
                <span className="detail-value">{detailModal.id}</span>
              </div>
              <div className="detail-row">
                <span className="detail-label">Brand Name:</span>
                <span className="detail-value">{detailModal.name}</span>
              </div>
              <div className="detail-row">
                <span className="detail-label">Logo:</span>
                <div className="detail-logo">
                  <img src={detailModal.logo} alt={detailModal.name} />
                </div>
              </div>
              <div className="detail-row">
                <span className="detail-label">Description:</span>
                <span className="detail-value">
                  {detailModal.description || "—"}
                </span>
              </div>
              <div className="detail-row">
                <span className="detail-label">Status:</span>
                <span className="status status-active">Active</span>
              </div>
              <div className="detail-row">
                <span className="detail-label">Number of Products:</span>
                <span className="detail-value">
                  {countProductsInBrand(detailModal.id)}
                </span>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Delete confirmation modal */}
      {deleteModal && (
        <div className="modal-overlay" onClick={() => setDeleteModal(null)}>
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
            <div className="modal-body">
              <p>
                Are you sure you want to delete brand{" "}
                <strong>{deleteModal.name}</strong>?
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

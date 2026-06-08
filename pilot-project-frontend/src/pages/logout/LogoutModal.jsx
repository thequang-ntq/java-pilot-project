import { useState } from "react";
import { useNavigate } from "react-router-dom";
import useAuth from "../../components/context/use-auth";
import "./LogoutModal.css";
import { logout } from "../../services/auth-api";

export default function LogoutModal({ onClose }) {
  const { logoutContext } = useAuth();
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);

  const handleConfirm = () => {
    setIsLoading(true);

    logout()
      .catch((err) => {
        console.error("Logout error:", err.userMessage);
      })
      .finally(() => {
        setIsLoading(false);
        logoutContext();
        onClose();
        navigate("/login", { replace: true });
      });
  };

  return (
    <div className="logout-overlay" onClick={onClose}>
      <div className="modal logout-modal" onClick={(e) => e.stopPropagation()}>
        {/* Loading spinner */}
        {isLoading && (
          <div className="loading-wrapper">
            <div className="spinner-border" role="status">
              <span className="visually-hidden">Loading...</span>
            </div>
          </div>
        )}
        <div className="modal-header">
          <h3 className="modal-title">Log out</h3>
          <button
            className="modal-close"
            onClick={onClose}
            disabled={isLoading}
          >
            &times;
          </button>
        </div>

        <div className="modal-body">
          <p>Are you sure you want to log out of your account?</p>
        </div>

        <div className="modal-footer">
          <button
            className="btn btn-secondary"
            onClick={onClose}
            disabled={isLoading}
          >
            Cancel
          </button>
          <button
            className="btn btn-danger"
            onClick={handleConfirm}
            disabled={isLoading}
          >
            Log out
          </button>
        </div>
      </div>
    </div>
  );
}

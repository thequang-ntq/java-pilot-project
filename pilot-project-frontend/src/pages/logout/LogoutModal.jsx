import { useNavigate } from "react-router-dom";
import useAuth from "../../components/context/use-auth";
import "./LogoutModal.css";
import { logout } from "../../services/auth-api";

export default function LogoutModal({ onClose }) {
  const { logoutContext } = useAuth();
  const navigate = useNavigate();

  const handleConfirm = () => {
    logout()
      .catch((err) => {
        console.error("Logout error:", err.userMessage);
      })
      .finally(() => {
        logoutContext();
        onClose();
        navigate("/login", { replace: true });
      });
  };

  return (
    <div className="logout-overlay" onClick={onClose}>
      <div className="logout-modal" onClick={(e) => e.stopPropagation()}>
        <h2 className="logout-modal-title">Log out?</h2>
        <p className="logout-modal-desc">
          Are you sure you want to log out of your account?
        </p>
        <div className="logout-modal-actions">
          <button
            className="logout-modal-btn logout-modal-btn-cancel"
            onClick={onClose}
          >
            Cancel
          </button>
          <button
            className="logout-modal-btn logout-modal-btn-confirm"
            onClick={handleConfirm}
          >
            Log out
          </button>
        </div>
      </div>
    </div>
  );
}

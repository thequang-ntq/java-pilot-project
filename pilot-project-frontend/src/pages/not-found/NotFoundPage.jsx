import "./NotFoundPage.css";
import { useNavigate } from "react-router-dom";
import NotFoundImage from "../../assets/404.gif";

export default function NotFoundPage() {
  const navigate = useNavigate();

  return (
    <div className="not-found-page">
      <div className="error">404</div>
      <div className="title">Page not found</div>
      <div className="content">
        Sorry, we couldn't find the page you're looking for.
      </div>
      <img className="image" alt="404 Image" src={NotFoundImage}></img>
      <button
        type="button"
        className="btn btn-dark"
        onClick={() => navigate("/home", { replace: true })}
      >
        Go Home
      </button>
    </div>
  );
}

import "bootstrap-icons/font/bootstrap-icons.css";
import "./Footer.css";

export default function Footer() {
  return (
    <footer>
      <div className="brand">
        <div className="logo">NTQ Shop</div>
        <div className="meta">
          <div className="title">Java Pilot Project</div>
          <div className="sub">© 2026 · Built with Spring Boot + React</div>
        </div>
      </div>

      <div className="social" aria-label="Social links">
        <a className="icon" href="#" aria-label="GitHub">
          <i className="bi bi-github" />
        </a>
        <a className="icon" href="#" aria-label="LinkedIn">
          <i className="bi bi-linkedin" />
        </a>
        <a className="icon" href="#" aria-label="Email">
          <i className="bi bi-envelope" />
        </a>
      </div>
    </footer>
  );
}

import { Link, NavLink, useNavigate } from "react-router-dom";
import { useState, useEffect, useRef } from "react";
import NoAvatar from "../../../assets/no-avatar.png";
import useAuth from "../../../context/use-auth";
import { logout } from "../../../services/auth-api";
import "./MainLayout.css";

export default function MainLayout({
  pageClassName,
  title,
  children,
  isAuthenticationPage,
  isLoading,
}) {
  const { auth } = useAuth(); // Auth context

  const [isOpen, setIsOpen] = useState(false); // check if drawer is opening
  const [dropdownOpen, setDropdownOpen] = useState(false); // check if dropdown if open
  const drawerRef = useRef(null);
  const dropdownRef = useRef(null);
  const { logoutContext } = useAuth();
  const navigate = useNavigate();
  const [isLogout, setIsLogout] = useState(false);

  // Close menu when resize to desktop
  useEffect(() => {
    const handleResize = () => {
      if (window.innerWidth >= 1024) setIsOpen(false);
    };
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  // Close drawer when click outside
  useEffect(() => {
    const handleClickOutside = (e) => {
      if (
        isOpen &&
        drawerRef.current &&
        !drawerRef.current.contains(e.target) &&
        !e.target.closest(".menu-btn")
      ) {
        setIsOpen(false);
      }
    };

    if (isOpen) {
      document.addEventListener("mousedown", handleClickOutside);
    }

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [isOpen]);

  // Close user dropdown when click outside
  useEffect(() => {
    const handleClickOutside = (e) => {
      if (
        dropdownOpen &&
        dropdownRef.current &&
        !dropdownRef.current.contains(e.target)
      ) {
        setDropdownOpen(false);
      }
    };
    if (dropdownOpen)
      document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, [dropdownOpen]);

  // Close menu when click NavLink or action in menu
  const closeMenu = () => setIsOpen(false);

  // Function to do when click Logout in dropdown
  const handleLogoutClick = () => {
    setDropdownOpen(false);
    setIsOpen(false);
    setIsLogout(true);

    logout()
      .catch((err) => {
        console.error("Logout error:", err.userMessage);
      })
      .finally(() => {
        setIsLogout(false);
        logoutContext();
        navigate("/login", { replace: true });
      });
  };

  // Shared nav links: desktop nav, mobile nav
  const navLinks = (
    <ul className="nav-list">
      <li>
        <NavLink
          to="/dashboard"
          className={({ isActive }) => `nav-link ${isActive ? "active" : ""}`}
        >
          Dashboard
        </NavLink>
      </li>
      <li>
        <NavLink
          to="/brands"
          className={({ isActive }) => `nav-link ${isActive ? "active" : ""}`}
        >
          Brands
        </NavLink>
      </li>
      <li>
        <NavLink
          to="/products"
          className={({ isActive }) => `nav-link ${isActive ? "active" : ""}`}
        >
          Products
        </NavLink>
      </li>
    </ul>
  );

  return (
    <>
      {/* Header */}
      {!isAuthenticationPage && (
        <header className="header">
          <div className="header-container">
            <div className="header-wrapper">
              {/* Desktop */}
              <nav className="desktop-nav">{navLinks}</nav>

              {/* Mobile/Small Tablet/Tablet menu button */}
              <button
                className={`menu-btn ${isOpen ? "is-open" : ""}`}
                onClick={() => setIsOpen(!isOpen)}
              >
                <span className="menu-btn-bar" />
                <span className="menu-btn-bar" />
                <span className="menu-btn-bar" />
              </button>

              {/* Mobile/Small Tablet/Tablet drawer */}
              <div
                ref={drawerRef}
                className={`mobile-menu ${isOpen ? "is-open" : ""}`}
              >
                {/* Drawer header: logo + close button */}
                <div className="mobile-menu-header">
                  {auth && (
                    <div className="mobile-user">
                      <img
                        src={NoAvatar}
                        alt="avatar"
                        className="user-avatar"
                      />
                      <span className="user-name">{auth.username}</span>
                    </div>
                  )}
                  <button className="close-btn" onClick={closeMenu}>
                    <span>&times;</span>
                  </button>
                </div>

                <div className="mobile-menu-divider"></div>

                {/* Mobile nav links */}
                <nav className="mobile-nav" onClick={closeMenu}>
                  {navLinks}
                </nav>

                <div className="mobile-menu-divider"></div>

                {/* Mobile/Small Tablet actions */}
                <div className="mobile-actions">
                  {auth ? (
                    <button
                      type="button"
                      className="btn btn-danger"
                      onClick={handleLogoutClick}
                    >
                      Logout
                    </button>
                  ) : (
                    <Link
                      to="/login"
                      className="btn btn-outline-light"
                      onClick={closeMenu}
                    >
                      Log in
                    </Link>
                  )}
                </div>
              </div>

              {/* Desktop/Tablet actions */}
              <div className="desktop-actions">
                {/* User dropdown */}
                {auth && (
                  <div className="user-dropdown" ref={dropdownRef}>
                    <button
                      className="user-trigger"
                      onClick={() => setDropdownOpen((o) => !o)}
                    >
                      <img
                        src={NoAvatar}
                        alt="avatar"
                        className="user-avatar"
                      />
                      <span className="user-name">{auth.username}</span>
                      <span className="user-caret">
                        {dropdownOpen ? "▲" : "▼"}
                      </span>
                    </button>

                    {dropdownOpen && (
                      <div className="user-menu">
                        <button
                          className="user-menu-item"
                          onClick={handleLogoutClick}
                        >
                          Logout
                        </button>
                      </div>
                    )}
                  </div>
                )}
                {/* Guest action button */}
                {!auth && (
                  <Link to="/login" className="btn btn-outline-light">
                    Log in
                  </Link>
                )}
              </div>
            </div>
          </div>
        </header>
      )}

      {/* Main body */}
      <main
        className={[
          pageClassName,
          isAuthenticationPage && "authentication-main",
        ]
          .filter(Boolean)
          .join(" ")}
      >
        {/* Loading spinner */}
        {(isLoading || isLogout) && (
          <div className="loading-wrapper">
            <div className="spinner-border" role="status">
              <span className="visually-hidden">Loading...</span>
            </div>
          </div>
        )}
        {!isAuthenticationPage && <h1 className="title">{title}</h1>}
        <div className="main-remains">{children}</div>
      </main>

      {/* Footer */}
      {!isAuthenticationPage && (
        <footer className="footer">
          <div className="footer-container">
            <div className="footer-wrapper">
              <div className="meta">
                <div className="title">Java Pilot Project</div>
                <div className="sub">
                  © 2026 · Built with Spring Boot + React
                </div>
              </div>
            </div>
          </div>
        </footer>
      )}
    </>
  );
}

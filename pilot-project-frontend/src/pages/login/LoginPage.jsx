import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import MainLayout from "../../components/layout/MainLayout/MainLayout";
import useAuth from "../../components/context/use-auth";
import "./LoginPage.css";
import { sanitize, filterInput } from "../../utils/utils";
import { login } from "../../services/auth-api";

export default function LoginPage() {
  const { loginContext } = useAuth();
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false); // Show password by eye toggle
  const [errors, setErrors] = useState({}); // Input fields error
  const [loginError, setLoginError] = useState(""); // Login error to notify
  const [isLoading, setIsLoading] = useState(false);

  // Validate not empty at input fields frontend
  const validate = () => {
    const errs = {};
    if (!username.trim()) errs.username = "Username is required.";
    if (!password.trim()) errs.password = "Password is required.";
    return errs;
  };

  // Set login error, get errors, set errors, format username and password before check login
  // If true then set login() functions in AuthContext.Provider and go to HomePage,
  // else set login error
  const handleSubmit = () => {
    setLoginError("");
    const errs = validate();
    if (Object.keys(errs).length > 0) {
      setErrors(errs);
      return;
    }
    setErrors({});

    // Formatted input to check
    const formatted = {
      username: sanitize(username),
      password: sanitize(password),
    };

    setIsLoading(true);

    // Check account
    login(formatted)
      .then((res) => {
        const { accessToken, refreshToken } = res.data.data;

        loginContext(accessToken, refreshToken);
        navigate("/dashboard", { replace: true });
      })
      .catch((err) => {
        setPassword("");
        setLoginError(err.userMessage || "An error occurred during login");
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  // Enter or click Login button to handle submit
  const handleKeyDown = (e) => {
    if (e.key === "Enter") handleSubmit();
  };

  // Delete old error when user begin to input again, delete submit error
  const clearError = (field) => {
    setErrors((prev) => ({ ...prev, [field]: "" }));
    setLoginError("");
  };

  return (
    <MainLayout
      pageClassName="login-page"
      isAuthenticationPage
      isLoading={isLoading}
    >
      {/* Card */}
      <section className="login-card">
        <div className="login-card-container">
          <div className="login-card-wrapper">
            {/* Header */}
            <div className="login-card-header">
              <h1 className="title">Welcome back</h1>
              <p className="sub">Sign in to your account to continue.</p>
            </div>

            {/* Login error */}
            {loginError && (
              <div className="alert alert-danger">{loginError}</div>
            )}

            {/* Form */}
            <div className="login-form">
              {/* Username */}
              <div className="field">
                <label className="field-label" htmlFor="login-username">
                  Username
                </label>
                <input
                  id="login-username"
                  className={`field-input ${errors.username ? "field-input-error" : ""}`}
                  type="text"
                  placeholder="Enter your username"
                  value={username}
                  onChange={(e) => {
                    setUsername(filterInput(e.target.value));
                    clearError("username");
                  }}
                  onKeyDown={handleKeyDown}
                  autoComplete="off"
                />
                {errors.username && (
                  <span className="field-error">{errors.username}</span>
                )}
              </div>

              {/* Password */}
              <div className="field">
                <label className="field-label" htmlFor="login-password">
                  Password
                </label>
                <div className="field-input-wrap">
                  <input
                    id="login-password"
                    className={`field-input ${errors.password ? "field-input-error" : ""}`}
                    type={showPassword ? "text" : "password"}
                    placeholder="Enter your password"
                    value={password}
                    onChange={(e) => {
                      setPassword(filterInput(e.target.value));
                      clearError("password");
                    }}
                    onKeyDown={handleKeyDown}
                    autoComplete="off"
                  />
                  <button
                    type="button"
                    className="field-eye"
                    onClick={() => setShowPassword((v) => !v)}
                    tabIndex={-1}
                  >
                    {showPassword ? (
                      <i className="bi bi-eye-slash"></i>
                    ) : (
                      <i className="bi bi-eye"></i>
                    )}
                  </button>
                </div>
                {errors.password && (
                  <span className="field-error">{errors.password}</span>
                )}
              </div>

              {/* Submit */}
              <button
                type="submit"
                className="login-btn"
                onClick={handleSubmit}
              >
                Log in
              </button>
            </div>

            {/* Footer links */}
            <div className="login-card-footer">
              <Link to="/dashboard" className="login-back">
                ← Back to Dashboard
              </Link>
            </div>
          </div>
        </div>
      </section>
    </MainLayout>
  );
}

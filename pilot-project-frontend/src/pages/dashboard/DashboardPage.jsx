import { useState, useEffect } from "react";
import MainLayout from "../../components/layout/MainLayout/MainLayout";
import "./DashboardPage.css";
import { getBrands } from "../../services/brands-api";
import { getProducts } from "../../services/products-api";
import { formatPrice } from "../../utils/utils";
import useAuth from "../../components/context/use-auth";
import { toast, Bounce } from "react-toastify";

export default function DashboardPage() {
  const { auth } = useAuth();
  const isAdmin = auth?.role === "ADMIN";

  // Stats
  const [stats, setStats] = useState({
    totalBrands: 0,
    totalProducts: 0,
    totalProductValue: 0,
    lowStockProducts: 0,
  });
  const [isLoading, setIsLoading] = useState(false); // Is loading spinner?
  const [error, setError] = useState(null); // Error response
  const [isError, setIsError] = useState(false); // Check if error (error while fetch data)

  // Load data
  useEffect(() => {
    setIsLoading(true);
    setError(null);

    // First, after Promise.all, brandResponse contains real data of brands,
    // productsResponse contains real data of products (totalElements, totalPages)
    Promise.all([
      getBrands({ page: 1, keyword: "" }),
      getProducts({ page: 1, keyword: "", priceFrom: null, priceTo: null }),
    ])
      .then(([brandsResponse, productsResponse]) => {
        setIsError(false);

        // Data in response model
        const brandsData = brandsResponse.data.data;
        const productsData = productsResponse.data.data;

        // Total elements
        const totalBrands = brandsData.totalElements || 0;
        const totalProducts = productsData.totalElements || 0;

        // All product page data
        // Call API for each page, fetchPromises contains Promises wait for API to complete
        // Ex: [Promise<[10 products from page i]>, ...]
        const allProductsPromises = [];
        for (let i = 1; i <= productsData.totalPages; i++) {
          allProductsPromises.push(
            getProducts({
              page: i,
              keyword: "",
              priceFrom: null,
              priceTo: null,
            }).then((res) => res.data.data.content),
          );
        }

        // Flat = rest parameter (...content)
        // Ex: allPages.forEach((content) => allProducts.push(...content));
        Promise.all(allProductsPromises).then((allPages) => {
          const allProducts = allPages.flat();

          // Calculate total money of all products
          const totalValue = allProducts.reduce(
            (sum, product) => sum + product.price * product.quantity,
            0,
          );

          // List products that quantity < 10
          const lowStock = allProducts.filter(
            (product) => product.quantity < 10,
          ).length;

          setStats({
            totalBrands,
            totalProducts,
            totalProductValue: totalValue,
            lowStockProducts: lowStock,
          });
        });
      })
      .catch((err) => {
        setError(err.userMessage || "Failed to load dashboard data");
        setIsError(true);
      })
      .finally(() => {
        setIsLoading(false);
      });
  }, []);

  // Show error with toast
  useEffect(() => {
    if (error) {
      toast.error(error, {
        position: "top-center",
        autoClose: 2500,
        hideProgressBar: false,
        closeOnClick: false,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light",
        transition: Bounce,
      });
      setError(null);
    }
  }, [error]);

  return (
    <MainLayout pageClassName="dashboard-page" isLoading={isLoading}>
      <section className="dashboard-header">
        <div className="dashboard-header-container">
          <div className="dashboard-header-wrapper">
            <h1 className="title">Dashboard</h1>
          </div>
        </div>
      </section>

      <section className="dashboard-content">
        <div className="dashboard-content-container">
          <div className="dashboard-content-wrapper">
            {!isLoading && isError && (
              <div className="not-found">Cannot load data for dashboard.</div>
            )}

            {!isLoading && !isError && (
              <div className="stats-grid">
                <div className="stat-card">
                  <div className="stat-icon">
                    <i className="bi bi-tag-fill"></i>
                  </div>
                  <div className="stat-info">
                    <h3 className="stat-value">{stats.totalBrands}</h3>
                    <p className="stat-label">Total Brands</p>
                  </div>
                </div>

                <div className="stat-card">
                  <div className="stat-icon">
                    <i className="bi bi-box-seam"></i>
                  </div>
                  <div className="stat-info">
                    <h3 className="stat-value">{stats.totalProducts}</h3>
                    <p className="stat-label">Total Products</p>
                  </div>
                </div>

                {isAdmin && (
                  <div className="stat-card">
                    <div className="stat-icon">
                      <i className="bi bi-cash"></i>
                    </div>
                    <div className="stat-info">
                      <h3 className="stat-value">
                        {formatPrice(stats.totalProductValue)}
                      </h3>
                      <p className="stat-label">Total Money of Products</p>
                    </div>
                  </div>
                )}

                {isAdmin && (
                  <div className="stat-card">
                    <div className="stat-icon">
                      <i className="bi bi-info-lg"></i>
                    </div>
                    <div className="stat-info">
                      <h3 className="stat-value">{stats.lowStockProducts}</h3>
                      <p className="stat-label">Low Stock Products (&lt; 10)</p>
                    </div>
                  </div>
                )}
              </div>
            )}
          </div>
        </div>
      </section>
    </MainLayout>
  );
}

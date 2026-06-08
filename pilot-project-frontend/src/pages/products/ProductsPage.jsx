import { useState } from "react";
import MainLayout from "../../components/layout/MainLayout/MainLayout";
import "./ProductsPage.css";
import NoProductImage from "../../assets/no-product-image.jpeg";
import Pagination from "../../components/common/Pagination/Pagination";
import { formatPrice, formatDate } from "../../utils/utils";

// Products template data
const SAMPLE_PRODUCTS = [
  {
    id: 1,
    name: "iPhone XS Max",
    price: 26990000,
    brand: "Apple",
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-12",
    description: "Made in USA",
  },
  {
    id: 2,
    name: "iPhone X",
    price: 21090000,
    brand: "Apple",
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-09",
    description: "Apple's aim with the iPhone X was to create an iPhone.",
  },
  {
    id: 3,
    name: "iPhone 8 Plus",
    price: 17980000,
    brand: "Apple",
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-09",
    description: "The iPhone 8 includes a 4.7-inch display.",
  },
  {
    id: 4,
    name: "iPhone 7 Plus",
    price: 16500000,
    brand: "Apple",
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-10",
    description: "The iPhone 7 measures in at 138.3mm tall.",
  },
  {
    id: 5,
    name: "Samsung Galaxy Note 10 Plus",
    price: 22390000,
    brand: "Samsung",
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "It runs on the Samsung Exynos 9 Octa 9825 Chipset.",
  },
  {
    id: 6,
    name: "Samsung Galaxy S10",
    price: 21500000,
    brand: "Samsung",
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "The Galaxy S10 isn't all that small, of course.",
  },
  {
    id: 7,
    name: "Samsung Galaxy S10 Plus",
    price: 21990000,
    brand: "Samsung",
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "The Galaxy S10+ is Samsung latest flagship for 2019.",
  },
  {
    id: 8,
    name: "Samsung Galaxy A70",
    price: 7990000,
    brand: "Samsung",
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "It is powered by 2GHz octa-core Qualcomm Snapdragon 675.",
  },
  {
    id: 9,
    name: "Samsung Galaxy Note 9",
    price: 20490000,
    brand: "Samsung",
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "Samsung Note version",
  },
  {
    id: 10,
    name: "iPhone 11 Pro Max",
    price: 42990000,
    brand: "Apple",
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "New IPhone",
  },
  {
    id: 11,
    name: "iPhone 11",
    price: 21990000,
    brand: "Apple",
    qty: 80,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "New version",
  },
  {
    id: 12,
    name: "iPhone 6S Plus",
    price: 8990000,
    brand: "Apple",
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-12",
    description: "Made in USA",
  },
  {
    id: 13,
    name: "Xiaomi Note 7",
    price: 4500000,
    brand: "Sony",
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "description",
  },
  {
    id: 14,
    name: "Huawei P30 Pro",
    price: 20690000,
    brand: "Huawei",
    qty: 120,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "Huawei made in China",
  },
  {
    id: 15,
    name: "Huawei P30",
    price: 15290000,
    brand: "Huawei",
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "Huawei made in China",
  },
  {
    id: 16,
    name: "Oppo Reno 10X",
    price: 19990000,
    brand: "Oppo",
    qty: 70,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "Oppo made in China",
  },
  {
    id: 17,
    name: "Oppo A9",
    price: 7890000,
    brand: "Oppo",
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "Oppo China",
  },
  {
    id: 18,
    name: "Oppo A7",
    price: 7000000,
    brand: "Oppo",
    qty: 50,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "Oppo China",
  },
  {
    id: 21,
    name: "Levono Laptop HP-16",
    price: 21000000,
    brand: "Asus",
    qty: 65,
    image: NoProductImage,
    saleDate: "2026-04-30",
    description: null,
  },
];

const PAGE_SIZE = 10;

export default function ProductsPage() {
  const [search, setSearch] = useState(""); // Search keyword
  const [priceFrom, setPriceFrom] = useState(""); // Price from
  const [priceTo, setPriceTo] = useState(""); // Price to

  // Search keyword, price from, price to that applied and begin search function
  const [appliedFilters, setAppliedFilters] = useState({
    search: "",
    priceFrom: "",
    priceTo: "",
  });
  const [page, setPage] = useState(1); // Current page
  const [selectedProduct, setSelectedProduct] = useState(null); //selected product

  // Set applied search filter = search keyword + priceFrom + priceTo, and currentPage = 1
  const handleSearch = () => {
    setAppliedFilters({ search, priceFrom, priceTo });
    setPage(1);
  };

  // Enter when input search fields will call the search function
  const handleKeyDown = (e) => {
    if (e.key === "Enter") handleSearch();
  };

  // List all products according to search filter (has brand name, product name, price from empty or >= price
  // from, price to empty or <= price to)
  const filtered = SAMPLE_PRODUCTS.filter((p) => {
    const q = appliedFilters.search.toLowerCase();
    const matchSearch =
      p.name.toLowerCase().includes(q) || p.brand.toLowerCase().includes(q);
    const matchFrom =
      appliedFilters.priceFrom === "" ||
      p.price >= Number(appliedFilters.priceFrom);
    const matchTo =
      appliedFilters.priceTo === "" ||
      p.price <= Number(appliedFilters.priceTo);
    return matchSearch && matchFrom && matchTo;
  });

  const totalPages = Math.max(1, Math.ceil(filtered.length / PAGE_SIZE)); // total pages
  const paged = filtered.slice((page - 1) * PAGE_SIZE, page * PAGE_SIZE); // List products show in current page

  return (
    <MainLayout pageClassName="products-page">
      {/* Page header */}
      <section className="products-header">
        <div className="products-header-container">
          <div className="products-header-wrapper">
            <h1 className="title">Our Products</h1>
            <p className="sub">
              Browse our full lineup of premium smartphones and devices.
            </p>
          </div>
        </div>
      </section>

      {/* Search & filter */}
      <section className="products-search">
        <div className="products-search-container">
          <div className="products-search-wrapper">
            {/* Row 1: single search input */}
            <div className="row">
              <input
                className="input input-full"
                type="text"
                placeholder="Search by product name or brand name…"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                onKeyDown={handleKeyDown}
              />
            </div>

            {/* Row 2: price text inputs + button */}
            <div className="row">
              <div className="input-group">
                <label className="input-label">Price From</label>
                <input
                  className="input"
                  type="number"
                  min="0"
                  placeholder="e.g. 5000000"
                  value={priceFrom}
                  onChange={(e) => setPriceFrom(e.target.value)}
                  onKeyDown={handleKeyDown}
                />
              </div>

              <div className="input-group">
                <label className="input-label">Price To</label>
                <input
                  className="input"
                  type="number"
                  min="0"
                  placeholder="e.g. 30000000"
                  value={priceTo}
                  onChange={(e) => setPriceTo(e.target.value)}
                  onKeyDown={handleKeyDown}
                />
              </div>

              <button className="btn" onClick={handleSearch}>
                Search
              </button>
            </div>
          </div>
        </div>
      </section>

      {/* Product list */}
      <section className="products-list">
        <div className="products-list-container">
          <div className="products-list-wrapper">
            {paged.length === 0 ? (
              <p className="not-found">No products found.</p>
            ) : (
              <div className="grid">
                {paged.map((product) => (
                  <div
                    key={product.id}
                    className="card"
                    onClick={() => setSelectedProduct(product)}
                    role="button"
                    tabIndex={0}
                    onKeyDown={(e) =>
                      e.key === "Enter" && setSelectedProduct(product)
                    }
                  >
                    <div className="card-image">
                      <img src={product.image} alt={product.name} />
                    </div>
                    <div className="card-body">
                      <span className="card-brand">{product.brand}</span>
                      <h3 className="card-name">{product.name}</h3>
                      <p className="card-price">{formatPrice(product.price)}</p>
                      <div className="card-meta">
                        <span className="card-qty">
                          In stock: {product.qty}
                        </span>
                        <span className="card-date">
                          {formatDate(product.saleDate)}
                        </span>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}

            {/* Pagination */}
            <Pagination totalPages={totalPages} page={page} setPage={setPage} />
          </div>
        </div>
      </section>

      {/* Detail Modal */}
      {selectedProduct && (
        <div
          className="products-modal-overlay"
          onClick={() => setSelectedProduct(null)}
        >
          <div className="products-modal" onClick={(e) => e.stopPropagation()}>
            <button
              className="products-modal-close"
              onClick={() => setSelectedProduct(null)}
            >
              &times;
            </button>
            <div className="products-modal-image">
              <img src={selectedProduct.image} alt={selectedProduct.name} />
            </div>
            <div className="products-modal-body">
              <span className="products-modal-brand">
                {selectedProduct.brand}
              </span>
              <h2 className="products-modal-name">{selectedProduct.name}</h2>
              <p className="products-modal-price">
                {formatPrice(selectedProduct.price)}
              </p>
              <div className="products-modal-row">
                <span className="products-modal-row-label">In Stock</span>
                <span className="products-modal-row-value">
                  {selectedProduct.qty}
                </span>
              </div>
              <div className="products-modal-row">
                <span className="products-modal-row-label">Sale Date</span>
                <span className="products-modal-row-value">
                  {formatDate(selectedProduct.saleDate)}
                </span>
              </div>
              {selectedProduct.description && (
                <p className="products-modal-desc">
                  {selectedProduct.description}
                </p>
              )}
            </div>
          </div>
        </div>
      )}
    </MainLayout>
  );
}

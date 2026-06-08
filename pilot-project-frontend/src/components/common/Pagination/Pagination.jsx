import { useState, useEffect } from "react";
import "./Pagination.css";

export default function Pagination({ totalPages, page, setPage }) {
  // 5 page numbers for desktop, otherwise 3
  const [isMobile, setIsMobile] = useState(window.innerWidth < 1024);

  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth < 1024);
    };

    window.addEventListener("resize", handleResize);

    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, []);
  const maxVisible = isMobile ? 3 : 5;

  const pageNumbers = () => {
    // Show all pages if total pages < number of max visible page
    if (totalPages <= maxVisible) {
      return Array.from({ length: totalPages }, (_, i) => i + 1);
    }

    // Need to show ...
    // Always keep first page and last page, so must minus 2 slots, the remaining for middle slots
    const slotsForMiddle = maxVisible - 2;

    // Number of pages need to show in each side of current page
    const pagesOnEachSide = Math.floor(slotsForMiddle / 2);

    // Start and end page of middle slots
    let start = page - pagesOnEachSide;
    let end = page + pagesOnEachSide;

    // Adjust if number of middle slots is even -> minus end by 1
    if (slotsForMiddle % 2 === 0) {
      end--;
    }

    // Prevent overflow left
    // Ex: page=3, start=1 (too near page 1) -> to right: start=2, end=6
    if (start <= 2) {
      start = 2;
      end = start + slotsForMiddle - 1;
    }

    // Prevent overflow right
    // Ex: page=18, totalPages=20, end=20 (too near last page) -> to left: end=19, start=15
    if (end >= totalPages - 1) {
      end = totalPages - 1;
      start = end - slotsForMiddle + 1;
    }

    // List page numbers, always has first page
    const pages = [1];

    // Left dots for spacing between 1 and start
    if (start > 2) {
      pages.push("...");
    }

    // Middle pages
    for (let i = start; i <= end; i++) {
      pages.push(i);
    }

    // Right dots for spacing between end and last page
    if (end < totalPages - 1) {
      pages.push("...");
    }

    // Always has last page
    pages.push(totalPages);

    return pages;
  };

  return (
    <>
      <section className="pagination-section">
        <div className="pagination-container">
          <div className="pagination-wrapper">
            {totalPages > 1 && (
              <div className="pagination">
                <button
                  className="pagination-btn"
                  disabled={page === 1}
                  onClick={() => setPage((p) => p - 1)}
                >
                  ‹ Prev
                </button>

                {/* Load page numbers */}
                {pageNumbers().map((num, idx) =>
                  num === "..." ? (
                    <span
                      key={`ellipsis-${idx}`}
                      className="pagination-ellipsis"
                    >
                      ...
                    </span>
                  ) : (
                    <button
                      key={num}
                      className={`pagination-dot ${page === num ? "active" : ""}`}
                      onClick={() => setPage(num)}
                    >
                      {num}
                    </button>
                  ),
                )}

                <button
                  className="pagination-btn"
                  disabled={page === totalPages}
                  onClick={() => setPage((p) => p + 1)}
                >
                  Next ›
                </button>
              </div>
            )}
          </div>
        </div>
      </section>
    </>
  );
}

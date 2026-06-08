// Delete all danger characters in string:< > " ' &, [] = list delete characters,
// g = global,change all characters found. Example: <script>... -> dangerous, hacker can use it to hack
// Also delete redundant space beginning/end of text, between characters.
// Handle basic XSS, render HTML, send backend, get database
export function sanitize(str) {
  return str
    .replace(/[<>"'`;/**/{}]/g, "")
    .replace(/\s+/g, " ")
    .trim();
}

// Replace special character with empty, multiple space with one space when enter input
export function filterInput(value) {
  return value.replace(/[<>"'`;/**/{}]/g, "").replace(/\s+/g, " ");
}

// Generate random UUID v4
export function generateUUID() {
  return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, (c) => {
    const r = (Math.random() * 16) | 0;
    return (c === "x" ? r : (r & 0x3) | 0x8).toString(16);
  });
}

// Save image file → returns object URL (simulates src/assets/ path in FE mock)
export function saveImageFile(file) {
  if (!file) return null;
  return URL.createObjectURL(file); // In real app this would be saved to src/assets/<uuid>.<ext>
}

// Format price to VND money format
export function formatPrice(price) {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(price);
}

// Format date to: dd/MM/yyyy
export function formatDate(dateStr) {
  if (!dateStr) return "—";
  return new Date(dateStr).toLocaleDateString("en-GB");
}

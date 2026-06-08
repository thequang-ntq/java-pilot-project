import NoBrandImage from "../assets/no-brand-image.jpg";
import NoProductImage from "../assets/no-product-image.jpeg";

// Brand data template
export let brands = [
  {
    id: 1,
    name: "Apple",
    logo: NoBrandImage,
    description: "Apple Inc, California",
    isDeleted: false,
  },
  {
    id: 2,
    name: "Samsung",
    logo: NoBrandImage,
    description: "Samsung Inc, Korea",
    isDeleted: false,
  },
  {
    id: 3,
    name: "Oppo",
    logo: NoBrandImage,
    description: "Oppo Inc, China",
    isDeleted: false,
  },
  {
    id: 4,
    name: "LG",
    logo: NoBrandImage,
    description: "LG Inc, Japan",
    isDeleted: false,
  },
  {
    id: 5,
    name: "Xiaomi",
    logo: NoBrandImage,
    description: "Xiaomi Inc, China",
    isDeleted: false,
  },
  {
    id: 6,
    name: "Sony",
    logo: NoBrandImage,
    description: "Sony Inc, Japan",
    isDeleted: false,
  },
  {
    id: 7,
    name: "Huawei",
    logo: NoBrandImage,
    description: "Huawei made in China",
    isDeleted: false,
  },
  {
    id: 8,
    name: "Vivo",
    logo: NoBrandImage,
    description: "Vivo Inc, China",
    isDeleted: false,
  },
  {
    id: 9,
    name: "HTC",
    logo: NoBrandImage,
    description: "HTC Inc, California",
    isDeleted: false,
  },
  {
    id: 10,
    name: "Asus",
    logo: NoBrandImage,
    description: "Asus Inc, China",
    isDeleted: false,
  },
  {
    id: 11,
    name: "Realme",
    logo: NoBrandImage,
    description: "Realme Inc, China",
    isDeleted: false,
  },
  {
    id: 15,
    name: "Levono 5",
    logo: NoBrandImage,
    description: "China",
    isDeleted: false,
  },
];

let brandNextId = 16;

// Brand services
export function getBrands() {
  return brands;
}

export function getBrandById(id) {
  return brands.find((b) => b.id === Number(id)) ?? null;
}

export function addBrand(data) {
  const newBrand = { ...data, id: brandNextId++, isDeleted: false };
  brands = [...brands, newBrand];
  return newBrand;
}

export function updateBrand(id, data) {
  brands = brands.map((b) => (b.id === Number(id) ? { ...b, ...data } : b));
  return brands.find((b) => b.id === Number(id));
}

export function softDeleteBrand(id) {
  brands = brands.map((b) =>
    b.id === Number(id) ? { ...b, isDeleted: true } : b,
  );
}

export function isBrandNameTaken(name, excludeId = null) {
  return brands.some(
    (b) =>
      b.name.toLowerCase() === name.toLowerCase() &&
      b.id !== Number(excludeId) &&
      !b.isDeleted,
  );
}

// Product data template
export let products = [
  {
    id: 1,
    name: "iPhone XS Max",
    price: 26990000,
    brandId: 1,
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-12",
    description: "Made in USA",
    isDeleted: false,
  },
  {
    id: 2,
    name: "iPhone X",
    price: 21090000,
    brandId: 1,
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-09",
    description: "Apple's aim with the iPhone X was to create an iPhone.",
    isDeleted: false,
  },
  {
    id: 3,
    name: "iPhone 8 Plus",
    price: 17980000,
    brandId: 1,
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-09",
    description: "The iPhone 8 includes a 4.7-inch display.",
    isDeleted: false,
  },
  {
    id: 4,
    name: "iPhone 7 Plus",
    price: 16500000,
    brandId: 1,
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-10",
    description: "The iPhone 7 measures in at 138.3mm tall.",
    isDeleted: false,
  },
  {
    id: 5,
    name: "Samsung Galaxy Note 10 Plus",
    price: 22390000,
    brandId: 2,
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "It runs on the Samsung Exynos 9 Octa 9825 Chipset.",
    isDeleted: false,
  },
  {
    id: 6,
    name: "Samsung Galaxy S10",
    price: 21500000,
    brandId: 2,
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "The Galaxy S10 isn't all that small.",
    isDeleted: false,
  },
  {
    id: 7,
    name: "Samsung Galaxy S10 Plus",
    price: 21990000,
    brandId: 2,
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "The Galaxy S10+ is Samsung latest flagship for 2019.",
    isDeleted: false,
  },
  {
    id: 8,
    name: "Samsung Galaxy A70",
    price: 7990000,
    brandId: 2,
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "Powered by 2GHz octa-core Qualcomm Snapdragon 675.",
    isDeleted: false,
  },
  {
    id: 9,
    name: "Samsung Galaxy Note 9",
    price: 20490000,
    brandId: 2,
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "Samsung Note version",
    isDeleted: false,
  },
  {
    id: 10,
    name: "iPhone 11 Pro Max",
    price: 42990000,
    brandId: 1,
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "New IPhone",
    isDeleted: false,
  },
  {
    id: 11,
    name: "iPhone 11",
    price: 21990000,
    brandId: 1,
    qty: 80,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "New version",
    isDeleted: false,
  },
  {
    id: 12,
    name: "iPhone 6S Plus",
    price: 8990000,
    brandId: 1,
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-12",
    description: "Made in USA",
    isDeleted: false,
  },
  {
    id: 13,
    name: "Xiaomi Note 7",
    price: 4500000,
    brandId: 6,
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "description",
    isDeleted: false,
  },
  {
    id: 14,
    name: "Huawei P30 Pro",
    price: 20690000,
    brandId: 9,
    qty: 120,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "Huawei made in China",
    isDeleted: false,
  },
  {
    id: 15,
    name: "Huawei P30",
    price: 15290000,
    brandId: 9,
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "Huawei made in China",
    isDeleted: false,
  },
  {
    id: 16,
    name: "Oppo Reno 10X",
    price: 19990000,
    brandId: 3,
    qty: 70,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "Oppo made in China",
    isDeleted: false,
  },
  {
    id: 17,
    name: "Oppo A9",
    price: 7890000,
    brandId: 3,
    qty: 100,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "Oppo China",
    isDeleted: false,
  },
  {
    id: 18,
    name: "Oppo A7",
    price: 7000000,
    brandId: 3,
    qty: 50,
    image: NoProductImage,
    saleDate: "2019-10-08",
    description: "Oppo China",
    isDeleted: false,
  },
  {
    id: 21,
    name: "Levono Laptop HP-16",
    price: 21000000,
    brandId: 10,
    qty: 65,
    image: NoProductImage,
    saleDate: "2026-04-30",
    description: null,
    isDeleted: false,
  },
];

let productNextId = 22;

// Product services
export function getProducts() {
  return products;
}

export function getProductById(id) {
  return products.find((p) => p.id === Number(id)) ?? null;
}

export function addProduct(data) {
  const newProduct = { ...data, id: productNextId++, isDeleted: false };
  products = [...products, newProduct];
  return newProduct;
}

export function updateProduct(id, data) {
  products = products.map((p) => (p.id === Number(id) ? { ...p, ...data } : p));
  return products.find((p) => p.id === Number(id));
}

export function softDeleteProduct(id) {
  products = products.map((p) =>
    p.id === Number(id) ? { ...p, isDeleted: true } : p,
  );
}

export function isProductNameTaken(name, excludeId = null) {
  return products.some(
    (p) =>
      p.name.toLowerCase() === name.toLowerCase() &&
      p.id !== Number(excludeId) &&
      !p.isDeleted,
  );
}

export function getBrandName(brandId) {
  return brands.find((b) => b.id === Number(brandId))?.name ?? "—";
}

export function countProductsInBrand(brandId) {
  return products.filter((p) => p.brandId === Number(brandId) && !p.isDeleted)
    .length;
}

import Header from "../Header/Header";
import Footer from "../Footer/Footer";
import "./MainLayout.css";

export default function MainLayout({ pageClassName, children }) {
  return (
    <>
      <Header />
      <main
        className={["container-fluid", pageClassName].filter(Boolean).join(" ")}
      >
        {children}
      </main>
      <Footer />
    </>
  );
}

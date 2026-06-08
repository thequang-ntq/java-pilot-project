import MainLayout from "../../components/layout/MainLayout/MainLayout";
import "./DashboardPage.css";
import { formatPrice } from "../../utils/utils";

export default function DashboardPage() {
  return (
    <MainLayout pageClassName="dashboard-page">
      <section className="dashboard-overview">
        <div className="overview-container">
          <div className="overview-wrapper">
            <div className="stat-card">
              <h2 className="value">{formatPrice(19350000)}</h2>
              <div className="detail">Today's revenue</div>
            </div>
            <div className="stat-card">
              <h2 className="value">{formatPrice(19350000)}</h2>
              <div className="detail">Today's revenue</div>
            </div>
            <div className="stat-card">
              <h2 className="value">{formatPrice(19350000)}</h2>
              <div className="detail">Today's revenue</div>
            </div>
            <div className="stat-card">
              <h2 className="value">{formatPrice(19350000)}</h2>
              <div className="detail">Today's revenue</div>
            </div>
            <div className="stat-card">
              <h2 className="value">{formatPrice(19350000)}</h2>
              <div className="detail">Today's revenue</div>
            </div>
            <div className="stat-card">
              <h2 className="value">{formatPrice(19350000)}</h2>
              <div className="detail">Today's revenue</div>
            </div>
            <div className="stat-card">
              <h2 className="value">{formatPrice(19350000)}</h2>
              <div className="detail">Today's revenue</div>
            </div>
            <div className="stat-card">
              <h2 className="value">{formatPrice(19350000)}</h2>
              <div className="detail">Today's revenue</div>
            </div>
            <div className="stat-card">
              <h2 className="value">{formatPrice(19350000)}</h2>
              <div className="detail">Today's revenue</div>
            </div>
            <div className="stat-card">
              <h2 className="value">{formatPrice(19350000)}</h2>
              <div className="detail">Today's revenue</div>
            </div>
            <div className="stat-card">
              <h2 className="value">{formatPrice(19350000)}</h2>
              <div className="detail">Today's revenue</div>
            </div>
            <div className="stat-card">
              <h2 className="value">{formatPrice(19350000)}</h2>
              <div className="detail">Today's revenue</div>
            </div>
            <div className="stat-card">
              <h2 className="value">{formatPrice(19350000)}</h2>
              <div className="detail">Today's revenue</div>
            </div>
          </div>
        </div>
      </section>
    </MainLayout>
  );
}

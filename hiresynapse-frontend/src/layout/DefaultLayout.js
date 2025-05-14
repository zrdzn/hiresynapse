import React from "react";
import {AppContent, AppFooter, AppHeader, AppSidebar,} from "../components/index";

const DefaultLayout = ({ authDetails }) => {
  return (
    <div>
      <AppSidebar />
      <div className="wrapper d-flex flex-column min-vh-100">
        <AppHeader authDetails={authDetails} />
        <div className="body flex-grow-1">
          <AppContent />
        </div>
        <AppFooter />
      </div>
    </div>
  );
};

export default DefaultLayout;

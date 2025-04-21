import React, {useEffect, useState} from "react";
import {AppContent, AppFooter, AppHeader, AppSidebar,} from "../components/index";
import {CSpinner} from "@coreui/react";
import {authService} from "../services/authService";
import {useLocation} from "react-router-dom";

const DefaultLayout = () => {
  const location = useLocation();
  const [authDetails, setAuthDetails] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    authService.getAuthDetails()
      .then((res) => {
        setAuthDetails(res.data);
        setLoading(false);
      })
      .catch(err => {
        const path = location.pathname;
        const isPublicRoute = path.startsWith('/apply');
        console.log(isPublicRoute)
        if (!isPublicRoute) {
          window.location.href = 'http://localhost:8080/oauth2/authorization/auth0';
        } else {
          setLoading(false);
        }
      });
  }, [location.pathname]);

  if (loading) {
    return (
      <div className="pt-3 text-center">
        <CSpinner />
      </div>
    );
  }

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

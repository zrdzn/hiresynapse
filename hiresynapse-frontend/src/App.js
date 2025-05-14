import React, {Suspense, useEffect, useState} from 'react'
import {HashRouter, Navigate, Route, Routes} from 'react-router-dom'
import {useSelector} from 'react-redux'

import {CSpinner, useColorModes} from '@coreui/react'
import './scss/style.scss'
import {Slide, ToastContainer} from 'react-toastify'
import {QueryClient, QueryClientProvider} from '@tanstack/react-query'
import {authService} from "./services/authService";

const DefaultLayout = React.lazy(() => import('./layout/DefaultLayout'))

const Apply = React.lazy(() => import('./views/pages/apply/Apply'))
const ApplyOffers = React.lazy(() => import('./views/pages/apply/ApplyOffers'))
const Page404 = React.lazy(() => import('./views/pages/page404/Page404'))
const Page500 = React.lazy(() => import('./views/pages/page500/Page500'))

const queryClient = new QueryClient()

const AuthenticatedRoute = ({ element, isAuthenticated }) => {
  return isAuthenticated
    ? element
    : (window.location.href = 'http://localhost:8080/oauth2/authorization/auth0');
};

const App = () => {
  const { isColorModeSet, setColorMode } = useColorModes('theme')
  const storedTheme = useSelector((state) => state.theme)
  const [authDetails, setAuthDetails] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    authService.getAuthDetails()
      .then(response => {
        setAuthDetails(response.data);
        setLoading(false);
      })
      .catch(error => {
        console.error(error)
        setLoading(false);
      });
  }, []);

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.href.split('?')[1])
    const theme = urlParams.get('theme') && urlParams.get('theme').match(/^[A-Za-z0-9\s]+/)[0]
    if (theme) {
      setColorMode(theme)
    }

    if (isColorModeSet()) {
      return
    }

    setColorMode(storedTheme)
  }, [])

  if (loading) {
    return (
      <div className="pt-3 text-center">
        <CSpinner color="primary" variant="grow" />
      </div>
    )
  }

  return (
    <QueryClientProvider client={queryClient}>
      <HashRouter>
        <Suspense
          fallback={
            <div className="pt-3 text-center">
              <CSpinner color="primary" variant="grow" />
            </div>
          }
        >
          <Routes>
            {/* AUTHENTICATED */}
            <Route path="/dashboard/*"
                   name="Dashboard"
                   element={<AuthenticatedRoute
                     element={<DefaultLayout authDetails={authDetails} />} isAuthenticated={authDetails !== null} /> } />

            {/* PUBLIC */}
            <Route path="/" element={<Navigate to="/public/apply" />} />
            <Route exact path="/public/apply" name="Apply list" element={<ApplyOffers />} />
            <Route exact path="/public/apply/:jobId" name="Apply for job" element={<Apply />} />
            <Route exact path="/404" name="Page 404" element={<Page404 />} />
            <Route exact path="/500" name="Page 500" element={<Page500 />} />
          </Routes>
        </Suspense>
      </HashRouter>
      <ToastContainer
        position="bottom-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick={false}
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="light"
        transition={Slide}
      />
    </QueryClientProvider>
  )
}

export default App

import React, {Suspense, useEffect} from 'react'
import {HashRouter, Route, Routes} from 'react-router-dom'
import {useSelector} from 'react-redux'

import {CSpinner, useColorModes} from '@coreui/react'
import './scss/style.scss'
import {Slide, ToastContainer} from 'react-toastify'
import {QueryClient, QueryClientProvider} from '@tanstack/react-query'

const DefaultLayout = React.lazy(() => import('./layout/DefaultLayout'))

const Apply = React.lazy(() => import('./views/pages/apply/Apply'))
const ApplyOffers = React.lazy(() => import('./views/pages/apply/ApplyOffers'))
const Page404 = React.lazy(() => import('./views/pages/page404/Page404'))
const Page500 = React.lazy(() => import('./views/pages/page500/Page500'))

const queryClient = new QueryClient()

const App = () => {
  const { isColorModeSet, setColorMode } = useColorModes('theme')
  const storedTheme = useSelector((state) => state.theme)

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
            <Route exact path="/apply/:jobId" name="Apply for job" element={<Apply />} />
            <Route exact path="/apply" name="Apply list" element={<ApplyOffers />} />
            <Route exact path="/404" name="Page 404" element={<Page404 />} />
            <Route exact path="/500" name="Page 500" element={<Page500 />} />
            <Route path="*" name="Home" element={<DefaultLayout />} />
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

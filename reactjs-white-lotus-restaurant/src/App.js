import React, { Suspense } from 'react'
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'

import { ToastContainer } from 'react-toastify'

// Toast
import 'react-toastify/dist/ReactToastify.css'

// theme
import ThemeProvider from 'theme'
import RequireAuth from 'RequireAuth'
import Unauthorized from 'components/Unauthorized'
import Layout from 'components/Layout'
import { ROLES } from 'constants/global'

const NotFound = React.lazy(() => import('components/NotFound'))
const Login = React.lazy(() => import('features/Login'))
const Home = React.lazy(() => import('features/Home'))
const Kitchen = React.lazy(() => import('features/Kitchen'))

export default function App() {
  return (
    <>
      <Suspense fallback={<div>Loading ...</div>}>
        <BrowserRouter>
          <ThemeProvider>
            <ToastContainer
              position="bottom-right"
              autoClose={1500}
              hideProgressBar={false}
              newestOnTop={false}
              closeOnClick
              rtl={false}
              pauseOnFocusLoss
              draggable
              pauseOnHover
              theme="colored"
            />
            <Routes>
              <Route path="/" element={<Layout />}>
                <Route path="/login" element={<Login />} />
                <Route path="/unauthorized" element={<Unauthorized />} />
                <Route path="/" element={<Navigate to="/home" />} />

                <Route
                  element={
                    <RequireAuth
                      allowedRoles={[ROLES.ADMIN, ROLES.CASHIER, ROLES.WAITER]}
                    />
                  }
                >
                  <Route path="home" element={<Home />} />
                </Route>

                <Route
                  element={
                    <RequireAuth
                      allowedRoles={[ROLES.ADMIN, ROLES.KITCHEN, ROLES.WAITER]}
                    />
                  }
                >
                  <Route path="kitchen" element={<Kitchen />} />
                </Route>

                <Route path="*" element={<NotFound />} />
              </Route>
            </Routes>
          </ThemeProvider>
        </BrowserRouter>
      </Suspense>
    </>
  )
}

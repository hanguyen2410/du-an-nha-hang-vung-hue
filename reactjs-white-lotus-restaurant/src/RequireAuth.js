import jwtDecode from 'jwt-decode'
import { useDispatch } from 'react-redux'
import { useLocation, Navigate, Outlet } from 'react-router-dom'

import { setAuth } from 'features/Login/authSlice'

const RequireAuth = ({ allowedRoles }) => {
  const location = useLocation()

  const dispatch = useDispatch()

  const accessToken = localStorage.getItem('wl_accessToken')

  let isAuth = false
  let tokenDecode = null

  if (accessToken) {
    try {
      tokenDecode = jwtDecode(accessToken)
      const exp = new Date(tokenDecode.exp * 1000).getTime()
      const timeNow = new Date().getTime()

      if (exp > timeNow) {
        isAuth = true
        const action = setAuth(tokenDecode)
        dispatch(action)
      }
    } catch (error) {
      console.log(error)
    }
  }

  return isAuth && allowedRoles.find((role) => tokenDecode?.role === role) ? (
    <Outlet />
  ) : tokenDecode?.fullName ? (
    <Navigate to="/unauthorized" state={{ from: location }} replace />
  ) : (
    <Navigate to="/login" state={{ from: location }} replace />
  )
}

export default RequireAuth

import { useEffect, useState } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import jwtDecode from 'jwt-decode'

// @mui
import {
  Link,
  Stack,
  IconButton,
  InputAdornment,
  TextField,
  Checkbox,
} from '@mui/material'
import { LoadingButton } from '@mui/lab'

// components
import Iconify from 'components/Iconify'

// Toast
import { toast } from 'react-toastify'
import { login } from '../authSlice'
import { useDispatch } from 'react-redux'
import { ROLES } from 'constants/global'

// ----------------------------------------------------------------------

export default function LoginForm() {
  const navigate = useNavigate()
  const location = useLocation()
  const from = location.state?.from?.pathname || '/'

  const dispatch = useDispatch()

  const [showPassword, setShowPassword] = useState(false)
  const [state, setState] = useState({
    username: '',
    password: '',
  })

  const [loading, setLoading] = useState(false)

  const [errors, setErrors] = useState({
    usernameErr: '',
    passwordErr: '',
    err: '',
  })

  const handleInputChange = (e) => {
    setState({
      ...state,
      [e.target.name]: e.target.value,
    })

    setErrors({
      ...errors,
      [`${e.target.name}Err`]: '',
      err: '',
    })
  }

  const handleClickLogin = async () => {
    setLoading(true)

    const loginAction = login(state)
    dispatch(loginAction)
      .unwrap()
      .then((data) => {
        const accessToken = data.token

        const token = jwtDecode(accessToken)
        const userRole = token.role

        localStorage.setItem('wl_accessToken', accessToken)

        toast.success('Đăng nhập thành công')

        setTimeout(() => {
          if (userRole !== ROLES.KITCHEN) {
            navigate(from, { replace: true })
          } else {
            navigate('/kitchen', { replace: true })
          }
        }, 1500)
      })
      .catch((error) => {
        console.log(error)
        if (error.response) {
          const { username, password } = error.response.data
          setErrors({
            ...errors,
            usernameErr: username,
            passwordErr: password,
            err: error.response.data.message,
          })
        } else {
          setErrors({
            ...errors,
            err: 'Lỗi hệ thống, vui lòng liên hệ admin !',
          })
        }
        setLoading(false)
      })
  }

  const { username } = state
  const { usernameErr, passwordErr, err } = errors

  return (
    <>
      <Stack spacing={3} sx={{ color: 'white' }}>
        <TextField
          sx={{ color: 'white', backgroundColor: 'white' }}
          error={Boolean(usernameErr)}
          helperText={usernameErr}
          name="username"
          type="email"
          label="Tên đăng nhập"
          value={username}
          onChange={handleInputChange}
        />

        <TextField
          sx={{ color: 'white', backgroundColor: 'white' }}
          error={Boolean(passwordErr)}
          helperText={passwordErr}
          name="password"
          label="Mật khẩu"
          type={showPassword ? 'text' : 'password'}
          onChange={handleInputChange}
          InputProps={{
            endAdornment: (
              <InputAdornment position="end">
                <IconButton
                  onClick={() => setShowPassword(!showPassword)}
                  edge="end"
                >
                  <Iconify
                    icon={showPassword ? 'eva:eye-fill' : 'eva:eye-off-fill'}
                  />
                </IconButton>
              </InputAdornment>
            ),
          }}
        />
        {err && (
          <TextField
            value={err}
            variant="filled"
            InputProps={{
              disableUnderline: true,
            }}
            sx={{
              backgroundColor: '#ffd5dd',

              input: {
                color: 'red',
                textAlign: 'center',
                paddingTop: '15px',
                paddingBottom: '15px',
              },
              borderRadius: '8px',
            }}
          />
        )}
      </Stack>

      <Stack
        direction="row"
        alignItems="center"
        justifyContent="space-between"
        sx={{ my: 2 }}
      >
        <Checkbox name="remember" label="Remember me" sx={{ color: 'white' }} />
        <Link variant="subtitle2" underline="hover" sx={{ color: 'white' }}>
          Quên mật khẩu?
        </Link>
      </Stack>

      <LoadingButton
        sx={{ color: 'white', backgroundColor: '#4b4086' }}
        loading={loading}
        fullWidth
        size="large"
        type="submit"
        variant="contained"
        onClick={handleClickLogin}
      >
        Đăng nhập
      </LoadingButton>
    </>
  )
}

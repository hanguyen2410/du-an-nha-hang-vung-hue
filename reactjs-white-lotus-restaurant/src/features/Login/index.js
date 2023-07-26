import { Helmet } from 'react-helmet-async'

// @mui
import { styled } from '@mui/material/styles'
import { Link, Container, Typography } from '@mui/material'

// components
import LoginForm from './components/LoginForm'

import Images from 'constants/images'

// ----------------------------------------------------------------------

const StyledRoot = styled('div')(({ theme }) => ({
  [theme.breakpoints.up('md')]: {
    display: 'flex',
  },
}))

const StyledSection = styled('div')(({ theme }) => ({
  width: '100%',
  maxWidth: 480,
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'center',
  boxShadow: theme.customShadows.card,
  backgroundColor: theme.palette.background.default,
}))

const StyledContent = styled('div')(({ theme }) => ({
  maxWidth: 480,
  margin: 'auto',
  minHeight: '100vh',
  display: 'flex',
  justifyContent: 'center',
  flexDirection: 'column',
  padding: theme.spacing(12, 0),
}))

// ----------------------------------------------------------------------

export default function LoginPage() {
  return (
    <>
      <Helmet>
        <title> Login | White Lotus </title>
      </Helmet>

      <StyledRoot
        sx={{ backgroundColor: '#7266ba', borderBottomLeftRadius: '100%' }}
      >
        <StyledSection sx={{ justifyContent: 'start' }}>
          <Typography variant="h3" sx={{ px: 5, mt: 10, mb: 5 }}>
            Chào mừng đến với White Lotus Restaurant!
          </Typography>
          <div className="d-flex justify-content-center">
            <img src={Images.LOGIN_LOGO} alt="login" style={{ width: 300 }} />
          </div>
        </StyledSection>

        <Container maxWidth="sm" sx={{ color: 'white' }}>
          <StyledContent>
            <Typography variant="h4" gutterBottom>
              Đăng nhập vào White Lotus Restaurant
            </Typography>

            <Typography variant="body2" sx={{ mb: 5 }}>
              Không có tài khoản? {''}
              <Link variant="subtitle2">Bắt đầu</Link>
            </Typography>

            <LoginForm />
          </StyledContent>
        </Container>
      </StyledRoot>
    </>
  )
}

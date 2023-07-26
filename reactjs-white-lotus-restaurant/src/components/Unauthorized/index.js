import { useNavigate } from 'react-router-dom'

import { Helmet } from 'react-helmet-async'

// @mui
import { styled } from '@mui/material/styles'
import { Button, Typography, Container } from '@mui/material'

// ----------------------------------------------------------------------

const StyledContent = styled('div')(({ theme }) => ({
  maxWidth: 480,
  margin: 'auto',
  minHeight: '100vh',
  display: 'flex',
  justifyContent: 'center',
  flexDirection: 'column',
  padding: theme.spacing(12, 0),
}))

const Unauthorized = () => {
  const navigate = useNavigate()

  const goBack = () => navigate(-1)

  return (
    <>
      <Helmet>
        <title> 403 Unauthorized </title>
      </Helmet>

      <Container>
        <StyledContent sx={{ textAlign: 'center', alignItems: 'center' }}>
          <Typography variant="h1" paragraph>
            Unauthorized
          </Typography>
          <Typography variant="h3" paragraph>
            You do not have access to the requested page.
          </Typography>

          <Button onClick={goBack} size="large" variant="contained">
            Go Back
          </Button>
        </StyledContent>
      </Container>
    </>
  )
}

export default Unauthorized

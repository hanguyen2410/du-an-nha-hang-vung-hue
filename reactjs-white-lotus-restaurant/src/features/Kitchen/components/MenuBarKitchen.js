import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import PropTypes from 'prop-types'

// @mui
import { Button, Popover } from '@mui/material'

// ----------------------------------------------------------------------
import MenuItem from '@mui/material/MenuItem'

// component
import Iconify from '../../../components/Iconify'
import { replace } from 'lodash'
import { clearData } from 'features/Login/authSlice'
import { useDispatch } from 'react-redux'

MenuBarKitchen.propTypes = {}

export default function MenuBarKitchen() {
  const dispatch = useDispatch()

  const [open, setOpen] = useState(null)
  const navigate = useNavigate()

  const handleOpenMenu = (event) => {
    setOpen(event.currentTarget)
  }

  const handleCloseMenu = () => {
    setOpen(null)
  }

  const handleLogout = () => {
    localStorage.removeItem('wl_accessToken')

    const action = clearData()
    dispatch(action)

    navigate('/login')
  }

  const handleBackHome = () => {
    navigate('/home')
  }

  return (
    <>
      {Boolean(open) ? (
        <Button
          variant="contained"
          sx={{
            position: 'absolute',
            minHeight: '35px',
            maxHeight: '35px',
            minWidth: '35px',
            maxWidth: '35px',
            padding: 0,
            textAlign: 'center',
            mt: '3px',
            right: '15px !important',

            borderRadius: '50%',
            backgroundColor: '#F9FAFB',
            color: '#7266ba',
            boxShadow: 'none',
            '&:hover': {
              backgroundColor: '#F9FAFB',
              color: '#7266ba',
            },
          }}
          onClick={handleOpenMenu}
        >
          <Iconify icon={'mdi:menu-open'} width={20} height={50} />
        </Button>
      ) : (
        <Button
          variant="contained"
          sx={{
            position: 'absolute',
            minHeight: '35px',
            maxHeight: '35px',
            minWidth: '35px',
            maxWidth: '35px',
            padding: 0,
            textAlign: 'center',
            mt: '3px',
            right: '15px !important',
            borderRadius: '50%',
            backgroundColor: '#7266ba',
            color: 'white',
            boxShadow: 'none',
            '&:hover': {
              backgroundColor: '#F9FAFB',
              color: '#7266ba',
            },
          }}
          onClick={handleOpenMenu}
        >
          <Iconify icon={'mdi:menu-open'} width={20} height={50} />
        </Button>
      )}

      <Popover
        open={Boolean(open)}
        anchorEl={open}
        onClose={handleCloseMenu}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
        transformOrigin={{ vertical: 'top', horizontal: 'right' }}
        PaperProps={{
          sx: {
            p: 1,
            width: 160,
            '& .MuiMenuItem-root': {
              px: 1,
              typography: 'body2',
              borderRadius: 0.75,
            },
          },
        }}
      >
        <MenuItem onClick={handleBackHome}>
          <Iconify icon={'ep:dish-dot'} sx={{ mr: 2 }} />
          Thu ngân
        </MenuItem>
        <MenuItem>
          <Iconify icon={'twemoji:card-file-box'} sx={{ mr: 2 }} />
          <Link to="/temp">Quản lý</Link>
        </MenuItem>
        <MenuItem>
          <Iconify icon={'flat-color-icons:edit-image'} sx={{ mr: 2 }} />
          Sửa thông tin
        </MenuItem>

        <MenuItem sx={{ color: 'error.main' }} onClick={() => handleLogout()}>
          <Iconify icon={'entypo:log-out'} sx={{ mr: 2 }} />
          Đăng xuất
        </MenuItem>
      </Popover>
    </>
  )
}

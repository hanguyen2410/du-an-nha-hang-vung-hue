import { useState } from 'react'

// @mui
import { Button, Popover } from '@mui/material'
// ----------------------------------------------------------------------

import MenuItem from '@mui/material/MenuItem'

import { useNavigate } from 'react-router-dom'

// component
import Iconify from 'components/Iconify'
import { clearData } from 'features/Login/authSlice'
import { useDispatch } from 'react-redux'

MenuBar.propTypes = {}

export default function MenuBar() {
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

  const handleNextKitchen = () => {
    navigate('/kitchen')
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
            // ml: 2,
            right: '0px !important',
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
            // ml: 2,
            right: '0 !important',
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
        <MenuItem onClick={handleNextKitchen}>
          <Iconify icon={'ep:dish-dot'} sx={{ mr: 2 }} />
          Nhà bếp
        </MenuItem>
        <MenuItem>
          <Iconify icon={'twemoji:card-file-box'} sx={{ mr: 2 }} />
          Quản lý
        </MenuItem>
        <MenuItem>
          <Iconify icon={'flat-color-icons:edit-image'} sx={{ mr: 2 }} />
          Sửa thông tin
        </MenuItem>

        <MenuItem sx={{ color: 'error.main' }} onClick={handleLogout}>
          <Iconify icon={'entypo:log-out'} sx={{ mr: 2 }} />
          Đăng xuất
        </MenuItem>
      </Popover>
    </>
  )
}

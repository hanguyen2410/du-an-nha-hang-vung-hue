import React from 'react'
import { useSelector } from 'react-redux'
import { Button } from '@mui/material'

import Iconify from 'components/Iconify'

import { toast } from 'react-toastify'

const active = {
  position: 'relative',
  mt: '4px',
  ml: '9px',
  borderRadius: '15px 15px 0 0',
  backgroundColor: '#F9FAFB',
  color: '#4b4086',
  boxShadow: 'none',
  '&:hover': {
    backgroundColor: '#F9FAFB',
  },
}

const deActive = {
  position: 'relative',
  mt: '4px',
  ml: '9px',
  borderRadius: '15px 15px 0 0',
  backgroundColor: '#5B509A',
  boxShadow: 'none',
  '&:hover': {
    backgroundColor: '#F9FAFB',
    color: '#4b4086',
  },
}

export default function TabTableAndMenu({ checkTab, setCheckTab, ...other }) {
  const restaurantsData = useSelector((state) => state.baseData.data)
  const currentTable = restaurantsData.currentTable

  const showTables = () => {
    setCheckTab({
      ...checkTab,
      table: true,
      menu: false,
      billHistory: false,
    })
  }

  const showMenu = () => {
    Object.keys(currentTable).length
      ? setCheckTab({
          ...checkTab,
          table: false,
          menu: true,
          billHistory: false,
        })
      : toast.warning('Vui lòng chọn bàn !')
  }

  const showBillHistory = () => {
    setCheckTab({
      ...checkTab,
      table: false,
      menu: false,
      billHistory: true,
    })
  }

  return (
    <>
      {checkTab.table ? (
        <Button variant="contained" sx={active}>
          <Iconify
            icon={'ic:twotone-restaurant-menu'}
            color="#4b4086"
            width={20}
            height={50}
            sx={{ mr: 1 }}
          />
          Phòng Bàn
        </Button>
      ) : (
        <Button variant="contained" onClick={showTables} sx={deActive}>
          <Iconify
            icon={'ic:twotone-restaurant-menu'}
            width={20}
            height={50}
            sx={{ mr: 1 }}
          />
          Phòng Bàn
        </Button>
      )}
      {checkTab.menu ? (
        <Button variant="contained" sx={active}>
          <Iconify
            icon={'ic:baseline-menu-book'}
            width={20}
            height={50}
            sx={{
              mr: 1,
            }}
          />
          Thực đơn
        </Button>
      ) : (
        <Button variant="contained" onClick={showMenu} sx={deActive}>
          <Iconify
            icon={'ic:baseline-menu-book'}
            width={20}
            height={50}
            sx={{
              mr: 1,
            }}
          />
          Thực đơn
        </Button>
      )}
      {checkTab.billHistory ? (
        <Button variant="contained" sx={active}>
          <Iconify
            icon={'icon-park-outline:bill'}
            width={20}
            height={50}
            sx={{
              mr: 1,
            }}
          />
          Hoá đơn
        </Button>
      ) : (
        <Button variant="contained" onClick={showBillHistory} sx={deActive}>
          <Iconify
            icon={'icon-park-outline:bill'}
            width={20}
            height={50}
            sx={{
              mr: 1,
            }}
          />
          Hoá đơn
        </Button>
      )}
    </>
  )
}

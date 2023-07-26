import { Typography } from '@mui/material'
import Iconify from 'components/Iconify'
import React from 'react'

const KitchenWaiterEmpty = () => {
  return (
    <>
      <Iconify
        icon={'noto-v1:fork-and-knife-with-plate'}
        width="100px"
        height={50}
        sx={{
          position: 'relative',
          ml: '41%',
          mt: '25%',
        }}
      />
      <Typography
        sx={{
          textAlign: 'center',
          fontSize: '20px',
          fontWeight: 'bolder',
          fontFamily: 'sans-serif',
        }}
      >
        Chưa có món nào
      </Typography>
      <Typography
        sx={{
          textAlign: 'center',
          fontSize: '14px',
          fontFamily: 'sans-serif',
        }}
      >
        Bếp đang pha chế
      </Typography>
    </>
  )
}

export default KitchenWaiterEmpty

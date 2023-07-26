import React from 'react'
import { Button } from '@mui/material'

import Iconify from 'components/Iconify'

export default function StaffInfo({ fullName }) {
  return (
    <Button
      variant="contained"
      sx={{
        position: 'absolute',
        height: '24px',
        mt: '8px',
        // ml: 41,
        right: '35px !important',
        borderRadius: '15px',
        backgroundColor: '#7266ba',
        color: '#F9FAFB',
        boxShadow: 'none',
        '&:hover': {
          backgroundColor: '#F9FAFB',
          color: '#7266ba',
        },
      }}
    >
      <Iconify
        icon={'mdi:calendar-user'}
        width={20}
        height={50}
        sx={{
          mr: 1,
        }}
      />
      {fullName}
    </Button>
  )
}

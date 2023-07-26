import React from 'react'
import { Typography, Stack, Box } from '@mui/material'

import Iconify from 'components/Iconify'

export default function Footer() {
  return (
    <Box
      sx={{
        width: '100%',
        textAlign: 'center',
        // height: '3vh',
        height: '35px',
        paddingTop: '8px',
        // pt: '4px',
        position: 'fixed',
        backgroundColor: '#7266ba',
        bottom: 0,
      }}
    >
      <Stack direction="row" alignItems="center" justifyContent="center">
        <Iconify
          icon={'ic:baseline-phone-callback'}
          color="white"
          width={20}
          height={50}
          sx={{
            mr: 1,
          }}
        />
        <Typography
          sx={{ color: 'white', fontFamily: 'initial', fontSize: 'small' }}
        >
          Hỗ trợ: 1900 1009
        </Typography>
        <Iconify
          icon={'mdi:address-marker'}
          color="white"
          width={20}
          height={50}
          sx={{
            mr: 1,
            ml: 5,
          }}
        />
        <Typography
          sx={{ color: 'white', fontFamily: 'initial', fontSize: 'small' }}
        >
          Địa chỉ liên hệ: 28 Nguyễn Tri Phương{' '}
        </Typography>
        <Iconify
          icon={'ion:mail-open-outline'}
          color="white"
          width={20}
          height={50}
          sx={{
            mr: 1,
            ml: 5,
          }}
        />
        <Typography
          sx={{ color: 'white', fontFamily: 'initial', fontSize: 'small' }}
        >
          white.lotus.restaurant.hbt@gmail.com
        </Typography>{' '}
      </Stack>
    </Box>
  )
}

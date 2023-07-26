import { Button } from '@mui/material'

import Iconify from 'components/Iconify'

export default function TabDone() {
  return (
    <Button
      variant="contained"
      sx={{
        position: 'relative',
        mt: '4px',
        ml: '10px',
        borderRadius: '15px 15px 0 0',
        backgroundColor: '#4B4086',
        boxShadow: 'none',
        '&:hover': {
          backgroundColor: '#F9FAFB',
          color: '#4b4086',
        },
      }}
    >
      <Iconify
        icon={'ic:round-table-restaurant'}
        width={20}
        height={50}
        sx={{ mr: 1 }}
      />
      Chờ phục vụ
    </Button>
  )
}

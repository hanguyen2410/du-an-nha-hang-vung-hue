import { Button } from '@mui/material'

import Iconify from 'components/Iconify'

export default function TabCookingAndGroupTable({
  checkMenu,
  showMenu,
  ...other
}) {
  return (
    <>
      {checkMenu ? (
        <Button
          variant="contained"
          onClick={showMenu}
          sx={{
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
          }}
        >
          <Iconify
            icon={'ic:twotone-restaurant-menu'}
            width={20}
            height={50}
            sx={{ mr: 1 }}
          />
          Theo món
        </Button>
      ) : (
        <Button
          variant="contained"
          sx={{
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
          }}
        >
          <Iconify
            icon={'ic:twotone-restaurant-menu'}
            color="#4b4086"
            width={20}
            height={50}
            sx={{ mr: 1 }}
          />
          Theo món
        </Button>
      )}
      {checkMenu ? (
        <Button
          variant="contained"
          sx={{
            position: 'relative',
            mt: '4px',
            ml: '10px',
            borderRadius: '15px 15px 0 0',
            backgroundColor: '#F9FAFB',
            color: '#4b4086',
            boxShadow: 'none',
            '&:hover': {
              backgroundColor: '#F9FAFB',
            },
          }}
        >
          <Iconify
            icon={'ic:baseline-menu-book'}
            width={20}
            height={50}
            sx={{
              mr: 1,
            }}
          />
          Theo bàn
        </Button>
      ) : (
        <Button
          variant="contained"
          onClick={showMenu}
          sx={{
            position: 'relative',
            mt: '4px',
            ml: '10px',
            borderRadius: '15px 15px 0 0',
            backgroundColor: '#5B509A',
            boxShadow: 'none',
            '&:hover': {
              backgroundColor: '#F9FAFB',
              color: '#4b4086',
            },
          }}
        >
          <Iconify
            icon={'ic:baseline-menu-book'}
            width={20}
            height={50}
            sx={{
              mr: 1,
            }}
          />
          Theo bàn
        </Button>
      )}
    </>
  )
}

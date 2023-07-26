import React from 'react'
import { Button } from '@mui/material'

import Iconify from 'components/Iconify'
import { useSelector } from 'react-redux'

export default function TabOrderItems({ orderTable, closeTable, ...other }) {
  const restaurantsData = useSelector((state) => state.baseData.data)
  const currentTable = restaurantsData.currentTable

  return (
    <>
      {Object.keys(currentTable).length ? (
        <Button
          variant="contained"
          sx={{
            position: 'relative',
            mt: '4px',
            ml: '10px',
            borderRadius: '15px 15px 0 0',
            backgroundColor: '#F9FAFB',
            color: '#4B4086',
            boxShadow: 'none',
            '&:hover': {
              backgroundColor: '#4B4086',
              color: '#F9FAFB',
            },
          }}
        >
          <Iconify
            icon={'ic:round-table-restaurant'}
            width={20}
            height={50}
            sx={{ mr: 1 }}
          />
          {currentTable ? (
            <>
              {currentTable.name}
              <Iconify
                icon="ph:x-circle-thin"
                sx={{
                  minWidth: '20px',
                  ml: '30px',
                  padding: 0,
                  color: 'red',
                }}
                onClick={closeTable}
              />
            </>
          ) : (
            'Bàn'
          )}
        </Button>
      ) : (
        <>
          <Button
            variant="contained"
            sx={{
              position: 'relative',
              mt: '4px',
              ml: '10px',
              borderRadius: '15px 15px 0 0',
              backgroundColor: '#4B4086',
              color: '#F9FAFB',
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
            Bàn
          </Button>
        </>
      )}
    </>
  )
}

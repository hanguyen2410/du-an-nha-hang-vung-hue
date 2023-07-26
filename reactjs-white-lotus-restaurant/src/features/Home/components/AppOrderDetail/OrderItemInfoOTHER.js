// @mui
import { Typography, Stack, TextField } from '@mui/material'
import { styled } from '@mui/material/styles'

// SweetAlert
import 'layouts/sweetalert.css'

import Iconify from 'components/Iconify'
import Helper from 'utils/Helper'

// ----------------------------------------------------------------------

const style = {
  position: 'relative',
  width: '100%',
  paddingTop: '4px',
  bgcolor: 'background.paper',
  borderRadius: '12px',
  marginBottom: '1px',
  border: '1px solid white',
  '&:hover': {
    border: '1px solid blue',
  },
}
const styleReadonly = {
  position: 'relative',
  width: '100%',
  paddingTop: '4px',
  bgcolor: 'background.paper',
  borderRadius: '12px',
  marginBottom: '1px',
  border: '1px solid white',
}
const StyledProductImg = styled('img')({
  top: 0,
  width: '100%',
  height: '100%',
  objectFit: 'cover',
  position: 'absolute',
  borderRadius: '12px',
})

// ----------------------------------------------------------------------

export default function OrderItemInfoOTHER({ index, item }) {
  return (
    <>
      <Stack direction="row" alignItems="center">
        <Typography
          id="transition-modal-title"
          variant="body2"
          sx={{
            color: '#53382c',
            lineHeight: 0.6,
            textTransform: 'uppercase',
            fontSize: '16px',
            fontWeight: 'bold',
            fontFamily: 'serif',
            mt: 1,
          }}
        >
          {item.productTitle}
        </Typography>
      </Stack>
      <Stack
        direction="row"
        alignItems="center"
        justifyContent="space-between"
        sx={{ paddingTop: '5px' }}
      >
        <Typography
          variant="body2"
          sx={{
            color: '#53382c',
            fontSize: '15px',
            fontFamily: 'sans-serif',
          }}
        >
          {`Giá: ${Helper.formatCurrencyToVND(item.price)}`}
        </Typography>

        <TextField
          value={item.quantity}
          disabled
          variant="filled"
          InputProps={{
            disableUnderline: true,
          }}
          inputProps={{
            pattern: '^[0-9]+$',
          }}
          sx={{
            position: 'absolute',
            ml: '230px',
            input: {
              color: '#53382c',
              backgroundColor: 'white',
              fontSize: '17px',
              padding: ' 0px',
              fontWeight: 500,
              fontFamily: 'sans-serif',
              textAlign: 'center',
              width: '100px',
            },
          }}
        />

        <Typography
          variant="body2"
          sx={{
            mr: '25px',
            color: '#53382c',
            fontSize: '17px',
            fontWeight: 600,
            fontFamily: 'sans-serif',
          }}
        >
          {Helper.formatCurrencyToVND(item.amount)}
        </Typography>
      </Stack>
      <Stack direction="row" alignItems="center">
        <Iconify icon="material-symbols:note-outline" sx={{ color: 'gray' }} />
        <Typography
          variant="body2"
          sx={{
            color: '#53382c',
            fontSize: '12px',
            fontFamily: 'sans-serif',
          }}
        >
          {item.note ? item.note : 'Không có ghi chú'}
        </Typography>
      </Stack>
    </>
  )
}

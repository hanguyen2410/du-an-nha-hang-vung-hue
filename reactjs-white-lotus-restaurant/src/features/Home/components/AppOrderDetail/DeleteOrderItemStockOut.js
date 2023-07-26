import 'layouts/sweetalert.css'

import Iconify from 'components/Iconify'

export default function DeleteOrderItemStockOut({
  index,
  item,
  handleClickDeleteItemStockOut,
}) {
  return (
    <Iconify
      icon={'material-symbols:close'}
      position="absolute"
      top="2px"
      right="10px"
      bgcolor="white"
      // color="#5A5A72"
      color="red"
      width={25}
      padding={0.4}
      cursor="pointer"
      sx={{
        border: 'solid 1px red',
        borderRadius: '2px',
        '&:hover': {
          // color: '#131318',
          // bgcolor: '#EBEBEF',
          // border: '1px solid #B9B9c6',
          color: '#fff',
          bgcolor: 'red',
          border: '1px solid #fff',
        },
      }}
      onClick={() => {
        handleClickDeleteItemStockOut(index, item)
      }}
    />
  )
}

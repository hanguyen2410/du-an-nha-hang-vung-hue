import React from 'react'
import {
  Grid,
  Typography,
  Stack,
  Divider,
  TextField,
  Button,
  Fade,
  Box,
  Modal,
} from '@mui/material'

import Iconify from 'components/Iconify'

import Helper from 'utils/Helper'
import { COUDINARY } from 'constants/global'
import { useDispatch, useSelector } from 'react-redux'
import {
  increaseQuantityCurrentOrderItem,
  decreaseQuantityCurrentOrderItem,
  changeQuantityCurrentOrderItem,
  setNoteCurrentOrderItem,
  addItem,
  updateItem,
  setTotalAmount,
} from 'features/Home/orderItemSlice'
import { toast } from 'react-toastify'

// ----------------------------------------------------------------------

export default function ModalOrder({
  openModalOrder,
  handleCloseMorderOrder,
  checkEditModal,
}) {
  // const dispatch = useDispatch()

  const orderItemData = useSelector((state) => state.orderItemData.data)

  const currentOrderItem = orderItemData.currentOrderItem

  return Object.keys(currentOrderItem).length ? (
    <ModalComponent
      openModalOrder={openModalOrder}
      currentOrderItem={currentOrderItem}
      handleCloseMorderOrder={handleCloseMorderOrder}
      checkEditModal={checkEditModal}
    />
  ) : (
    <></>
  )
}

function ModalComponent({
  openModalOrder,
  currentOrderItem,
  handleCloseMorderOrder,
  checkEditModal,
}) {
  const dispatch = useDispatch()

  // Tăng số lượng modal chọn món
  const handleIncreaseQuantity = () => {
    const increaseQuantityCurrentOrderItemAction =
      increaseQuantityCurrentOrderItem()
    dispatch(increaseQuantityCurrentOrderItemAction)
  }

  // Giảm số lượng modal chọn món
  const handleDecreaseQuantity = () => {
    const decreaseQuantityCurrentOrderItemAction =
      decreaseQuantityCurrentOrderItem()
    dispatch(decreaseQuantityCurrentOrderItemAction)
  }

  // Thay đổi số lượng modal chọn món
  const handleChangeQuantity = (e) => {
    const quantity = +e.target.value

    if (quantity > 0 && quantity < 100) {
      const action = changeQuantityCurrentOrderItem(quantity)
      dispatch(action)
    }
  }

  const handleChangeNote = (e) => {
    const note = e.target.value
    const action = setNoteCurrentOrderItem(note)
    dispatch(action)
  }

  // Thêm món ăn
  const handleOrderProduct = async () => {
    if (checkEditModal) {
      dispatch(updateItem())
      toast.success('Sửa món thành công')
    } else {
      dispatch(addItem())
      toast.success('Thêm món thành công')
    }

    dispatch(setTotalAmount())

    // setCheckEditModal(false)
    // checkEditModal = false

    handleCloseMorderOrder()
  }

  return (
    <Modal
      aria-labelledby="transition-modal-title"
      aria-describedby="transition-modal-description"
      open={openModalOrder}
      onClose={handleCloseMorderOrder}
      closeAfterTransition
      disableAutoFocus
    >
      <Fade in={openModalOrder}>
        <Box
          sx={{
            position: 'absolute',
            top: '50%',
            left: '50%',
            transform: 'translate(-50%, -50%)',
            width: 800,
            bgcolor: 'background.paper',
            border: '2px solid #d8d8df',
            borderRadius: '12px',
            boxShadow: 24,
            p: 3,
          }}
        >
          <Iconify
            icon={'material-symbols:close'}
            position="absolute"
            top="-10px"
            right="-10px"
            bgcolor="white"
            color="#5A5A72"
            borderRadius="50%"
            border="1px solid #d8d8df"
            width={36}
            padding={0.6}
            cursor="pointer"
            boxShadow="rgba(0, 0, 0, 0.2) 0px 2px 12px 0px"
            sx={{
              '&:hover': {
                color: '#131318',
                bgcolor: '#EBEBEF',
                border: '1px solid #B9B9c6',
              },
            }}
            onClick={handleCloseMorderOrder}
          />
          <Typography
            id="transition-modal-title"
            variant="h6"
            component="h2"
            sx={{
              color: '#53382c',
              textTransform: 'uppercase',
              fontWeight: 'bolder',
              fontFamily: 'fontkhachhang',
            }}
          >
            {currentOrderItem.productTitle}
          </Typography>
          <Grid container spacing={3}>
            <Grid item xs={5} md={5} lg={5}>
              <Box sx={{ position: 'relative' }}>
                <img
                  alt={currentOrderItem.productTitle}
                  src={
                    COUDINARY.url +
                    '/' +
                    COUDINARY.SCALE_IMAGE_300_300 +
                    '/' +
                    currentOrderItem.productAvatar.fileFolder +
                    '/' +
                    currentOrderItem.productAvatar.fileName
                  }
                  sx={{
                    top: 0,
                    width: '100%',
                    height: '100%',
                    objectFit: 'cover',
                    position: 'absolute',
                  }}
                />
              </Box>
              <Stack
                direction="row"
                alignItems="center"
                justifyContent="center"
                mt={2}
              >
                <Typography
                  variant="transition-modal-description"
                  sx={{
                    mr: 3,
                    fontSize: '14px',
                    color: '#333333',
                    fontWeight: 'normal',
                  }}
                >
                  TỔNG CỘNG:
                </Typography>

                <Typography
                  sx={{
                    color: '#b22830',
                    fontWeight: 700,
                    fontFamily: 'sans-serif',
                  }}
                >
                  {Helper.formatCurrencyToVND(
                    currentOrderItem.quantity * currentOrderItem.price
                  )}
                </Typography>
              </Stack>
            </Grid>
            <Grid item xs={7} md={7} lg={7} sx={{ textAlign: 'justify' }}>
              <Typography
                variant="transition-modal-description"
                sx={{
                  fontSize: '14px',
                  color: '#53382c',
                  fontWeight: 'normal',
                  lineHeight: 1,
                }}
              >
                {currentOrderItem.description}
              </Typography>
              <Divider sx={{ mb: 2 }} />
              <Stack direction="row" alignItems="center" sx={{ mb: 2 }}>
                <Typography
                  variant="transition-modal-description"
                  sx={{
                    mr: 1,
                    fontSize: '14px',
                    color: '#333333',
                    fontWeight: 'normal',
                  }}
                >
                  Giá:
                </Typography>

                <Typography
                  sx={{
                    color: '#53382c',
                    fontWeight: 700,
                    fontFamily: 'sans-serif',
                  }}
                >
                  {Helper.formatCurrencyToVND(currentOrderItem.price)}
                </Typography>
              </Stack>
              <Stack direction="row" alignItems="center" sx={{ mb: 2 }}>
                <Typography
                  variant="transition-modal-description"
                  sx={{
                    mr: 1,
                    fontSize: '14px',
                    color: '#333333',
                    fontWeight: 'normal',
                  }}
                >
                  Số lượng:
                </Typography>
                <TextField
                  inputProps={{ pattern: '^[0-9]+$' }}
                  value={currentOrderItem.quantity}
                  size="small"
                  sx={{
                    ml: 2,
                    input: {
                      color: '#53382c',
                      fontWeight: 700,
                      fontFamily: 'sans-serif',
                      textAlign: 'center',
                      width: '150px',
                    },
                  }}
                  onChange={handleChangeQuantity}
                />
                <Button
                  disabled={currentOrderItem.quantity <= 1}
                  sx={{
                    color: '#f42525',
                    cursor: 'pointer',
                    ml: 12,
                    borderRadius: '50%',
                    maxWidth: '30px',
                    minWidth: '35px',
                    maxHeight: '30px',
                    minHeight: '35px',
                    border: 'none',
                    fontWeight: '20px',
                    position: 'absolute',
                    '&:hover': {
                      backgroundColor: '#ff563014',
                      border: 'none',
                    },
                  }}
                  onClick={handleDecreaseQuantity}
                >
                  <Iconify icon="ic:outline-minus" />
                </Button>
                <Button
                  disabled={currentOrderItem.quantity >= 99}
                  sx={{
                    color: '#0dbd16',
                    cursor: 'pointer',
                    ml: 28,
                    borderRadius: '50%',
                    maxWidth: '30px',
                    minWidth: '35px',
                    maxHeight: '30px',
                    minHeight: '35px',
                    border: 'none',
                    position: 'absolute',
                    '&:hover': {
                      backgroundColor: '##cedfcf',
                      border: 'none',
                    },
                  }}
                  onClick={handleIncreaseQuantity}
                >
                  <Iconify icon="ic:baseline-plus" />
                </Button>
              </Stack>

              <Stack direction="row" alignItems="center" sx={{ mb: 2 }}>
                <Typography
                  variant="transition-modal-description"
                  sx={{
                    mr: 1,
                    fontSize: '14px',
                    color: '#333333',
                    fontWeight: 'normal',
                  }}
                >
                  Đơn vị:
                </Typography>
                <Typography
                  variant="transition-modal-description"
                  sx={{
                    mr: 1,
                    ml: 4,
                    fontSize: '18px',
                    fontWeight: '600',
                    color: '#333333',
                    // fontWeight: 'normal',
                  }}
                >
                  {currentOrderItem.unitTitle}
                </Typography>
              </Stack>

              <TextField
                id="standard-basic"
                label="Ghi chú"
                variant="standard"
                value={currentOrderItem.note}
                onChange={handleChangeNote}
                sx={{
                  input: {
                    fontFamily: 'sans-serif',
                    width: '400px',
                  },
                }}
              />
              <Button
                variant="contained"
                sx={{
                  color: 'white',
                  backgroundColor: '#4B4086',
                  border: '1px solid #4B4086',
                  mt: 2,
                  ml: '36%',
                  '&:hover': {
                    backgroundColor: 'white',
                    color: '#4B4086',
                  },
                }}
                onClick={handleOrderProduct}
              >
                Đặt món ngay
              </Button>
            </Grid>
          </Grid>
        </Box>
      </Fade>
    </Modal>
  )
}

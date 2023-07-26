import { useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'

// import PropTypes from 'prop-types'

import { Typography, Grid, Stack, Box } from '@mui/material'

import { LoadingButton } from '@mui/lab'

import Iconify from 'components/Iconify'

import {
  changeCookingToWaiterAllProductOfProductGroup,
  changeCookingToWaiterOneProductOfProductGroup,
} from 'features/Kitchen/kitchenSlice'

import { HTTP_STATUS } from 'constants/global'
import { toast } from 'react-toastify'

import authService from 'services/authService'

const accessToken = localStorage.getItem('wl_accessToken')

AppOrderItemList.propTypes = {
  // orderItems: PropTypes.array.isRequired,
  // onOrder: PropTypes.func,
}

const style = {
  position: 'relative',
  width: '100%',
  paddingTop: '4px',
  bgcolor: 'background.paper',
  borderRadius: '12px',
  marginBottom: '1px',
  border: '1px solid white',
  '&:hover': {
    border: '1px solid #7266BA',
  },
}

// ----------------------------------------------------------------------

export default function AppOrderItemList({ orderItemsCooking }) {
  const socket = authService.connectServer()

  const dispatch = useDispatch()
  const kitchenDataStatus = useSelector((state) => state.kitchenData.status)

  const [state, setState] = useState({
    indexLoading: -1,
  })

  const handleClickOneByProduct = (productId, productTitle, note, index) => {
    setState({
      ...state,
      indexLoading: productId + '_' + index,
    })

    const obj = {
      productId,
      note,
    }

    dispatch(changeCookingToWaiterOneProductOfProductGroup(obj))
      .unwrap()
      .then((data) => {
        const tableName = data.tableName
        const tableId = data.tableId

        const tableObj = {
          tableId,
          tableName,
        }

        socket.emit(
          'change-cooking-to-waiter-success-one-product-of-product',
          tableObj,
          accessToken
        )

        // socket.emit('change-cooking-to-waiter-success-of-kitchen', accessToken)

        toast.success(`Thêm 1 '${productTitle}' của ${tableName} đã làm xong`)
      })
      .catch(() => {
        toast.error(`Chuyển trạng thái món ăn thất bại`)
      })
      .finally(() => {
        setState({
          ...state,
          indexLoading: -1,
        })
      })
  }

  const handleClickAllByProduct = (productId, productTitle, note, index) => {
    setState({
      ...state,
      indexLoading: productId + '_' + index,
    })

    const obj = {
      productId,
      note,
    }

    dispatch(changeCookingToWaiterAllProductOfProductGroup(obj))
      .unwrap()
      .then((data) => {
        socket.emit(
          'change-cooking-to-waiter-success-all-product-of-product',
          accessToken
        )

        // socket.emit('change-cooking-to-waiter-success-of-kitchen', accessToken)

        toast.success(`'${productTitle}' đã làm xong`)
      })
      .catch(() => {
        toast.error(`Chuyển trạng thái món ăn '${productTitle}' thất bại`)
      })
      .finally(() => {
        setState({
          ...state,
          indexLoading: -1,
        })
      })
  }

  const { indexLoading } = state

  return (
    <>
      {orderItemsCooking.map((orderItem, index) => (
        <Box key={orderItem.productId + '_' + index} sx={style}>
          <Grid>
            <Stack
              direction="row"
              alignItems="center"
              justifyContent="space-between"
              sx={{
                padding: '12px 5px',
                border: '1px solid white',
                borderRadius: '12px',
              }}
            >
              <Stack
                direction="column"
                alignItems="flex-start"
                sx={{
                  mr: 3,
                }}
              >
                <Stack direction="row" alignItems="center">
                  <Typography
                    id="transition-modal-title"
                    variant="body2"
                    sx={{
                      color: '#5B509A',
                      lineHeight: 0.6,
                      textTransform: 'uppercase',
                      fontSize: '16px',
                      fontWeight: 'bold',
                      fontFamily: 'serif',
                      mt: 1,
                    }}
                  >
                    {orderItem.productTitle}
                  </Typography>
                  <Typography
                    variant="body2"
                    align="center"
                    sx={{
                      color: '#d52323',
                      fontFamily: 'sans-serif',
                      ml: 1,
                    }}
                  >
                    {orderItem.unitTitle}
                  </Typography>
                </Stack>

                <Stack direction="row" alignItems="center">
                  <Typography
                    id="transition-modal-title"
                    variant="body2"
                    sx={{
                      color: '#a30101',
                      lineHeight: 0.6,
                      fontSize: '16px',
                      fontWeight: 'bold',
                      fontFamily: 'serif',
                      mt: 2,
                    }}
                  >
                    {orderItem.note}
                  </Typography>
                </Stack>
              </Stack>

              <Stack direction="row" alignItems="center">
                <Typography
                  id="transition-modal-title"
                  variant="body2"
                  sx={{
                    color: 'black',
                    fontSize: '16px',
                    fontWeight: 'bold',
                    fontFamily: 'serif',
                    mr: 10,
                    height: 45,
                    display: 'flex',
                    alignItems: 'center',
                  }}
                >
                  {orderItem.quantity}
                </Typography>

                <LoadingButton
                  loading={
                    kitchenDataStatus === HTTP_STATUS.PENDING &&
                    orderItem.productId + '_' + index === indexLoading
                  }
                  variant="contained"
                  sx={{
                    borderRadius: '20px',
                    backgroundColor: '#F9FAFB',
                    color: '#5B509A',
                    boxShadow: 'none',
                    border: '1px solid #5B509A',
                    mr: 1,
                    '&:hover': {
                      backgroundColor: '#5B509A',
                      color: 'white',
                    },
                  }}
                  onClick={() => {
                    handleClickOneByProduct(
                      orderItem.productId,
                      orderItem.productTitle,
                      orderItem.note,
                      index
                    )
                  }}
                >
                  <Iconify icon={'ic:sharp-keyboard-arrow-right'} width={20} />
                </LoadingButton>
                <LoadingButton
                  // loading={loadingAllProduct && index === indexLoadingAllProduct}
                  loading={
                    kitchenDataStatus === HTTP_STATUS.PENDING &&
                    orderItem.productId + '_' + index === indexLoading
                  }
                  variant="contained"
                  sx={{
                    borderRadius: '20px',
                    backgroundColor: '#5B509A',

                    color: 'white',
                    boxShadow: 'none',
                    border: '1px solid #5B509A',
                    '&:hover': {
                      backgroundColor: '#F9FAFB',
                      color: '#5B509A',
                    },
                  }}
                  onClick={() => {
                    handleClickAllByProduct(
                      orderItem.productId,
                      orderItem.productTitle,
                      orderItem.note,
                      index
                    )
                  }}
                >
                  <Iconify icon={'ic:outline-double-arrow'} width={20} />
                </LoadingButton>
              </Stack>
            </Stack>
          </Grid>
        </Box>
      ))}
    </>
  )
}

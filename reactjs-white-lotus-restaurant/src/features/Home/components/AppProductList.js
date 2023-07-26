import { useDispatch, useSelector } from 'react-redux'

import {
  Box,
  Card,
  Typography,
  Stack,
  Grid,
  Tooltip,
  tooltipClasses,
  Button,
} from '@mui/material'

import { styled } from '@mui/material/styles'

import Helper from 'utils/Helper'

import { COUDINARY } from 'constants/global'

import Iconify from 'components/Iconify'

import Swal from 'sweetalert2'

import {
  setDataLoading,
  setOutStockProduct,
  restoreOutStockProduct,
} from 'features/Home/restaurantSlice'

const LightTooltip = styled(({ className, ...props }) => (
  <Tooltip {...props} classes={{ popper: className }} />
))(({ theme }) => ({
  [`& .${tooltipClasses.tooltip}`]: {
    backgroundColor: theme.palette.common.white,
    color: 'rgba(0, 0, 0, 0.87)',
    boxShadow: theme.shadows[1],
    fontSize: 14,
  },
}))

// ----------------------------------------------------------------------

export default function AppProductList({
  handleOpenModalOrder,
  checkSearchEmpty,
  ...other
}) {
  const dispatch = useDispatch()
  const restaurantsData = useSelector((state) => state.baseData.data)

  const productsData = restaurantsData.products
  const productsSearchData = restaurantsData.productsSearch

  let productsSearch

  if (productsSearchData.length) {
    productsSearch = productsSearchData
  } else if (checkSearchEmpty) {
    productsSearch = []
  } else {
    productsSearch = productsData
  }

  const handleSetOutStockProduct = (product) => {
    Swal.fire({
      title: `Bạn có chắc muốn đóng món ${product.productTitle} vì hết hàng?`,
      text: 'Kiểm tra kỹ thông tin và tình trạng món ăn trước khi xác nhận',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Đồng ý',
      cancelButtonText: 'Hủy',
    }).then((result) => {
      if (result.isConfirmed) {
        dispatch(setDataLoading(true))
        dispatch(setOutStockProduct(product.productId)).then(() => {
          dispatch(setDataLoading(false))
        })
      }
    })
  }

  const handleRestoreProduct = (product) => {
    Swal.fire({
      title: `Bạn có chắc muốn mở bán ${product.productTitle}?`,
      text: 'Kiểm tra kỹ thông tin và tình trạng món ăn trước khi xác nhận',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Đồng ý',
      cancelButtonText: 'Hủy',
    }).then((result) => {
      if (result.isConfirmed) {
        dispatch(setDataLoading(true))
        dispatch(restoreOutStockProduct(product.productId)).then(() => {
          dispatch(setDataLoading(false))
        })
      }
    })
  }

  return (
    <Grid container spacing={3} {...other} sx={{ p: 2 }}>
      {productsSearch &&
        productsSearch.map((item) => (
          <Grid key={item.productId} item xs={3} sm={3} md={3}>
            <Card
              sx={{
                '&:hover': { boxShadow: '0px 2px 5px rgb(98 108 116 / 20%)' },
              }}
            >
              {item.outStock && (
                <div
                  style={{
                    position: 'absolute',
                    width: '100%',
                    height: '100%',
                    backgroundColor: 'rgba(0,0,0,0.3)',
                    zIndex: 100000,
                    display: 'flex',
                    flexDirection: 'column',
                    justifyContent: 'center',
                    alignItems: 'center',
                  }}
                >
                  <p
                    style={{
                      fontSize: '20px',
                      fontWeight: '700',
                      color: 'red',
                    }}
                  >
                    HẾT HÀNG
                  </p>
                  <Button
                    sx={{
                      color: 'white',
                      backgroundColor: '#7266ba',
                      width: '100px',
                      height: '36px',
                      padding: '6px 12px',
                      fontSize: '16px',
                      '&:hover': {
                        color: '#white',
                        backgroundColor: '#5b509a',
                      },
                    }}
                    onClick={() => {
                      handleRestoreProduct(item)
                    }}
                  >
                    Mở bán
                  </Button>
                </div>
              )}
              <Iconify
                icon={'material-symbols:close'}
                title="Đóng món"
                placement="top"
                position="absolute"
                top="5px"
                right="5px"
                width={25}
                padding={0.4}
                cursor="pointer"
                sx={{
                  borderRadius: '50%',
                  color: 'transparent',
                  '&:hover': {
                    color: 'white',
                    backgroundColor: '#7266ba',
                    border: '1px solid white',
                  },
                  zIndex: 100000,
                }}
                onClick={() => {
                  handleSetOutStockProduct(item)
                }}
              />
              <Box
                sx={{
                  pt: '100%',
                  position: 'relative',
                  cursor: 'pointer',
                }}
              >
                <img
                  style={{
                    top: 0,
                    width: '100%',
                    height: '100%',
                    objectFit: 'cover',
                    position: 'absolute',
                  }}
                  alt={item.productTitle}
                  src={
                    COUDINARY.url +
                    '/' +
                    COUDINARY.SCALE_IMAGE_180_180 +
                    '/' +
                    item.productAvatar.fileFolder +
                    '/' +
                    item.productAvatar.fileName
                  }
                  onClick={() => {
                    handleOpenModalOrder(item.productId)
                  }}
                />
              </Box>
              <Stack
                spacing={2}
                sx={{ pb: 1, pl: 0.5, pr: 0.5, textAlign: 'center' }}
              >
                <LightTooltip title={item.productTitle} placement="top">
                  <Typography
                    variant="subtitle2"
                    noWrap
                    sx={{
                      color: '#4b4086',
                      textTransform: 'uppercase',
                      fontWeight: 'bolder',
                      fontFamily: 'fontkhachhang',
                      cursor: 'pointer',
                      '&:hover': {
                        color: '#f14a50',
                      },
                    }}
                    onClick={() => {
                      handleOpenModalOrder(item.productId)
                    }}
                  >
                    {item.productTitle}
                  </Typography>
                </LightTooltip>

                <Stack
                  direction="row"
                  alignItems="center"
                  justifyContent="space-between"
                  sx={{
                    textTransform: 'uppercase',
                    fontWeight: 'bolder',
                    color: '#4b4086',
                    mt: '0px !important',
                    fontFamily: 'sans-serif',
                  }}
                >
                  <Typography
                    variant="subtitle2"
                    sx={{
                      fontWeight: 'bolder',
                      color: '#4b4086',
                      fontFamily: 'sans-serif',
                    }}
                  >
                    &nbsp;
                    {Helper.formatCurrencyToVND(item.price)}
                  </Typography>
                  <Typography
                    sx={{
                      color: '#4b4086',
                      fontFamily: 'fontkhachhang',
                    }}
                  >
                    {item.unitTitle}
                  </Typography>
                </Stack>
              </Stack>
            </Card>
          </Grid>
        ))}
    </Grid>
  )
}

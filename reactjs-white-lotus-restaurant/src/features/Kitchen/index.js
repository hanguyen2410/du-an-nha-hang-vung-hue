import { useState, useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { Helmet } from 'react-helmet-async'

// @mui
import { Box, Grid, Container } from '@mui/material'

// SweetAlert
import 'layouts/sweetalert.css'

// Toast
import { toast } from 'react-toastify'

import Loading from 'features/Home/loading/Loading'

import Footer from 'features/Home/components/Footer'

import TabCookingAndGroupTable from './components/TabCookingAndGroupTable'
import TabDone from './components/TabDone'
import StaffInfo from 'features/Home/components/StaffInfo'
import MenuBarKitchen from './components/MenuBarKitchen'
import AppWaiterList from './components/AppWaiterList'

import {
  fetchAllKitchenData,
  updateKitchenData,
  setKitchenDataLoading,
} from 'features/Kitchen/kitchenSlice'

import KitchenWaiterEmpty from './components/KitchenWaiterEmpty'
import GroupProductAndTableCooking from './components/BodyCooking'

import authService from 'services/authService'

// ----------------------------------------------------------------------

let ts
let tingSound

export default function Kitchen() {
  const dispatch = useDispatch()

  const socket = authService.connectServer()

  socket.on('get-all-data-kitchen', (res) => {
    if (ts !== res.ts) {
      ts = res.ts

      const action = updateKitchenData(res.data)
      dispatch(action)
    }
  })

  const authData = useSelector((state) => state.authData.data)
  const fullName = authData.fullName

  const kitchenData = useSelector((state) => state.kitchenData.data)
  const orderItemsWaiter = kitchenData.orderItemsWaiter
  const kitchenDataLoading = kitchenData.loading

  const [state, setState] = useState({
    loadingAllProduct: false,
    loadingAllTable: false,
    loadingAllProductTable: false,
    loadingAllWaiter: false,
    loadingOneProduct: false,
    loadingOneProductTable: false,
    loadingOneWaiter: false,
  })

  // Khởi tạo dữ liệu
  useEffect(() => {
    const getData = () => {
      dispatch(setKitchenDataLoading(true))

      dispatch(fetchAllKitchenData())
        .unwrap()
        .catch(() => {
          toast.warning('Vui lòng kiểm tra kết nối mạng !')
        })
        .finally(() => {
          dispatch(setKitchenDataLoading(false))
        })
    }

    getData()
  }, [])

  const [checkMenu, setCheckMenu] = useState(false)

  const showMenu = () => {
    setCheckMenu(!checkMenu)
  }

  return (
    <>
      <Helmet>
        <title> White Lotus | Nhà bếp</title>
      </Helmet>

      {kitchenDataLoading && <Loading />}

      <Container
        maxWidth="100%"
        sx={{ backgroundColor: '#7266BA', height: '100%' }}
      >
        <Grid container spacing={2}>
          <Grid item xs={6} md={6} lg={6}>
            <TabCookingAndGroupTable
              checkMenu={checkMenu}
              showMenu={showMenu}
            />
            <Box
              style={{
                // height: 430,
                height: '90vh',
                overflow: 'hidden',
                // overflowY: 'scroll',
                padding: '10px',
                backgroundColor: '#F9FAFB',
                borderRadius: '12px',
              }}
              className="wrapperBoard"
            >
              <GroupProductAndTableCooking checkMenu={!checkMenu} />
            </Box>
          </Grid>

          <Grid item xs={6} md={6} lg={6}>
            <Box
              style={{
                position: 'relative',
              }}
              className="wrapperBoard"
            >
              <TabDone />
              <StaffInfo fullName={fullName} />
              <MenuBarKitchen />
            </Box>

            <Box
              style={{
                // height: 555,
                height: '90vh',
                padding: '10px 10px 10px 10px',
                backgroundColor: '#EFF0F1',
                borderRadius: '12px',
                // verflow: 'hidden',
                // overflowY: 'scroll',
              }}
              className="wrapperBoard"
            >
              <Box
                style={{
                  height: '100%',
                  paddingRight: '5px',
                  overflow: 'auto',
                  overflowY: 'scroll',
                  // backgroundColor: '#0008100a',
                }}
                className="wrapperBoard"
              >
                {orderItemsWaiter.length ? (
                  <AppWaiterList />
                ) : (
                  <KitchenWaiterEmpty />
                )}
              </Box>
            </Box>
          </Grid>
        </Grid>
      </Container>

      <Footer />
    </>
  )
}

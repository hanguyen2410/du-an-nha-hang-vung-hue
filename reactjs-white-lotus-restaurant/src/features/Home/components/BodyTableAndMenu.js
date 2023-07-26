import React, { useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'

import { Stack, Box } from '@mui/material'

import AppSearchProduct from './AppSearchProduct'
import AppTablesList from './AppTableList'
import AppProductList from './AppProductList'
import AppBillHistoryList from './AppBillHistoryList'
import { setProductsSearch } from 'features/Home/restaurantSlice'

export default function BodyTableAndMenu({
  checkMenu,
  handleOpenModalOrder,
  handleSelectTable,
  checkTab,
  ...other
}) {
  const dispatch = useDispatch()
  const restaurantsData = useSelector((state) => state.baseData.data)
  const categories = restaurantsData.categories
  const products = restaurantsData.products
  let productList = products

  const [state, setState] = useState({
    keyword: '',
    categoryId: 0,
  })

  const [checkSearchEmpty, setCheckSearchEmpty] = useState(false)

  const handleSearch = async (e) => {
    setState({
      ...state,
      keyword: e.target.value,
    })

    if (categoryId === 0 && !e.target.value) {
      productList = products
    } else if (!e.target.value) {
      productList = products.filter((item) => item.category.id === categoryId)
    } else if (categoryId === 0) {
      productList = products.filter((item) =>
        item.productTitle.toUpperCase().includes(e.target.value.toUpperCase())
      )
    } else {
      productList = products.filter(
        (item) =>
          item.productTitle
            .toUpperCase()
            .includes(e.target.value.toUpperCase()) &&
          item.category.id === categoryId
      )
    }

    dispatch(setProductsSearch(productList))

    if (productList.length === 0) {
      setCheckSearchEmpty(true)
    }
  }

  const handleChangeCategory = async (e) => {
    setState({
      ...state,
      categoryId: +e.target.value,
    })
    if (!keyword && +e.target.value === 0) {
      productList = products
    } else if (!keyword) {
      productList = products.filter(
        (item) => item.category.id === +e.target.value
      )
    } else if (+e.target.value === 0) {
      productList = products.filter((item) =>
        item.productTitle.toUpperCase().includes(keyword.toUpperCase())
      )
    } else {
      productList = products.filter(
        (item) =>
          item.productTitle.toUpperCase().includes(keyword.toUpperCase()) &&
          item.category.id === +e.target.value
      )
    }

    dispatch(setProductsSearch(productList))

    if (productList.length === 0) {
      setCheckSearchEmpty(true)
    }
  }

  const doSearch = () => {
    if (categoryId === 0) {
      productList = products.filter((item) =>
        item.productTitle.toUpperCase().includes(keyword.toUpperCase())
      )
    } else {
      productList = products.filter(
        (item) =>
          item.productTitle.toUpperCase().includes(keyword.toUpperCase()) &&
          item.category.id === categoryId
      )
    }

    dispatch(setProductsSearch(productList))

    if (productList.length === 0) {
      setCheckSearchEmpty(true)
    }
  }

  const { keyword, categoryId } = state

  return (
    <Box
      style={{
        // height: 555,
        height: '90vh',
        overflow: 'hidden',
        padding: '10px',
        backgroundColor: '#F9FAFB',
        borderRadius: '12px',
      }}
      className="wrapperBoard"
    >
      {checkTab.table && (
        <Box
          style={{
            height: '100%',
            overflow: 'auto',
            backgroundColor: '#F9FAFB',
            borderRadius: '12px',
            marginTop: '20px',
            paddingBottom: '15px',
          }}
          className="wrapperBoard"
        >
          <AppTablesList
            handleSelectTable={handleSelectTable}
            title="DANH SÁCH"
          />
        </Box>
      )}
      {checkTab.menu && (
        <>
          <Stack
            direction="row"
            alignItems="center"
            justifyContent="flex-end"
            sx={{
              width: '100%',
              height: 40,
            }}
          >
            <AppSearchProduct
              categories={categories}
              keyword={keyword}
              categoryId={categoryId}
              onFilterName={handleSearch}
              doSearch={doSearch}
              handleChangeCategory={handleChangeCategory}
            />
          </Stack>
          <Box
            style={{
              // height: 495,
              height: '100%',
              overflow: 'auto',
              backgroundColor: '#F9FAFB',
              borderRadius: '12px',
              marginTop: '20px',
              // marginBottom: '60px',
              paddingBottom: '60px',
            }}
            className="wrapperBoard"
          >
            <AppProductList
              checkSearchEmpty={checkSearchEmpty}
              handleOpenModalOrder={(productId) => {
                handleOpenModalOrder(productId)
              }}
            />
          </Box>
        </>
      )}
      {checkTab.billHistory && (
        <Box
          style={{
            height: '100%',
            overflow: 'auto',
            backgroundColor: '#F9FAFB',
            borderRadius: '12px',
            marginTop: '20px',
            paddingBottom: '15px',
          }}
          className="wrapperBoard"
        >
          <AppBillHistoryList />
        </Box>
      )}
    </Box>
  )
}

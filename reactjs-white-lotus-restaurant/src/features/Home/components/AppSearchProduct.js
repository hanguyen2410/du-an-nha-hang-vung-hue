import PropTypes from 'prop-types'
// @mui
import { styled, alpha } from '@mui/material/styles'
import { Toolbar, OutlinedInput } from '@mui/material'
// component
import Iconify from 'components/Iconify'
// ----------------------------------------------------------------------

const StyledRoot = styled(Toolbar)(({ theme }) => ({
  // marginLeft: '350px',
  minHeight: '0px !important',
  display: 'flex',
  justifyContent: 'space-between',
  padding: theme.spacing(0, 1, 0, 3),
}))

const StyledSearch = styled(OutlinedInput)(({ theme }) => ({
  width: 240,
  transition: theme.transitions.create(['box-shadow', 'width'], {
    easing: theme.transitions.easing.easeInOut,
    duration: theme.transitions.duration.shorter,
  }),
  '&.Mui-focused': {
    width: 320,
    boxShadow: theme.customShadows.z8,
  },
  '& fieldset': {
    borderWidth: `1px !important`,
    borderColor: `${alpha(theme.palette.grey[500], 0.32)} !important`,
  },
}))

// ----------------------------------------------------------------------

AppSearchProduct.propTypes = {
  categories: PropTypes.array,
  keyword: PropTypes.string,
  handleSearch: PropTypes.func,
  onFilterName: PropTypes.func,
  onDeleteSearch: PropTypes.func,
}

export default function AppSearchProduct({
  categories,
  keyword,
  handleChangeCategory,
  onFilterName,
  doSearch,
}) {
  return (
    <div className="col-12 d-flex ms-auto">
      <div className="me-2" style={{ marginLeft: 'auto' }}>
        <input
          type="text"
          id="keyWord"
          value={keyword}
          onChange={onFilterName}
          className="form-control"
          placeholder="Tìm kiếm..."
        />
      </div>
      <div className="me-2">
        <select
          className="form-select select2-container"
          id="categoryFilter"
          onChange={handleChangeCategory}
        >
          {categories &&
            categories.map((cate) => (
              <option key={cate.id} value={cate.id}>
                {cate.title}
              </option>
            ))}
        </select>
      </div>
      <div className="me-2">
        <button
          className="btn"
          id="btnSearch"
          style={{
            border: '1px solid #7266ba',
            color: '#4b4086',
            '&:hover': {
              backgroundColor: 'rgb(114 102 186)',
              color: '#fff',
            },
          }}
          onClick={doSearch}
        >
          <Iconify icon={'il:search'} cursor="pointer" />
          Tìm kiếm
        </button>
      </div>
    </div>
  )
}

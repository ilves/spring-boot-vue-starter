<template lang="pug">
  nav
    ul.pagination
      li(:class="['page-item', {disabled:firstPageSelected}]")
        a.page-link(@click="prevPage($event)", @keyup.enter="prevPage($event)", tabindex="0", href="#")
          | Previous
      li(v-for="page in pages", :class="['page-item', {active:page.selected, disabled:page.disabled}]")
        a.page-link(@click="handlePageSelected(page.index, $event)",
                    @keyup.enter="handlePageSelected(page.index, $event)", tabindex="0", href="#")
          | {{page.content}}
      li(:class="['page-item', {disabled:lastPageSelected}]")
        a.page-link(@click="nextPage($event)", @keyup.enter="nextPage($event)", tabindex="0", href="#")
          | Next
</template>

<script>
  export default {
    props: {
      pageCount: {
        type: Number,
        required: true
      },
      page: {
        type: Number,
        default: 0
      },
      clickHandler: {
        type: Function,
        default: () => { }
      },
      pageRange: {
        type: Number,
        default: 3
      },
      marginPages: {
        type: Number,
        default: 1
      }
    },
    computed: {
      pages: function () {
        let items = {}
        if (this.pageCount <= this.pageRange) {
          for (let index = 0; index < this.pageCount; index++) {
            items[index] = {
              index: index,
              content: index + 1,
              selected: index === this.page
            }
          }
        } else {
          let leftPart = this.pageRange / 2
          let rightPart = this.pageRange - leftPart
          if (this.page < leftPart) {
            leftPart = this.page
            rightPart = this.pageRange - leftPart
          } else if (this.page > this.pageCount - this.pageRange / 2) {
            rightPart = this.pageCount - this.page
            leftPart = this.pageRange - rightPart
          }
          // items logic extracted into this function
          let mapItems = index => {
            let page = {
              index: index,
              content: index + 1,
              selected: index === this.page
            }
            if (index <= this.marginPages - 1 || index >= this.pageCount - this.marginPages) {
              items[index] = page
              return
            }
            let breakView = {
              content: '...',
              disabled: true
            }
            if ((this.page - leftPart) > this.marginPages && items[this.marginPages] !== breakView) {
              items[this.marginPages] = breakView
            }
            if ((this.page + rightPart) < (this.pageCount - this.marginPages - 1) && items[this.pageCount - this.marginPages - 1] !== breakView) {
              items[this.pageCount - this.marginPages - 1] = breakView
            }
            let overCount = this.page + rightPart - this.pageCount + 1
            if (overCount > 0 && index === this.page - leftPart - overCount) {
              items[index] = page
            }
            if ((index >= this.page - leftPart) && (index <= this.page + rightPart)) {
              items[index] = page
              return
            }
          }
          // 1st - loop thru low end of margin pages
          for (let i = 0; i < this.marginPages; i++) {
            mapItems(i)
          }
          // 2nd - loop thru high end of margin pages
          for (let i = this.pageCount - 1; i >= this.pageCount - this.marginPages; i--) {
            mapItems(i)
          }
          // 3rd - loop thru selected range
          let selectedRangeLow = 0
          if (this.page - this.pageRange > 0) {
            selectedRangeLow = this.initialPage - this.pageRange
          }
          let selectedRangeHigh = this.pageCount
          if (this.page + this.pageRange < this.pageCount) {
            selectedRangeHigh = this.page + this.pageRange
          }
          for (let i = selectedRangeLow; i < selectedRangeHigh; i++) {
            mapItems(i)
          }
        }
        return items
      },
      firstPageSelected () {
        return this.page === 0
      },
      lastPageSelected () {
        return (this.page === this.pageCount - 1) || (this.pageCount === 0)
      }
    },
    methods: {
      prevPage (e) {
        e.preventDefault()
        if (this.page <= 0) {
          return
        }
        this.clickHandler(this.page - 1)
      },
      nextPage (e) {
        e.preventDefault()
        if (this.page >= this.pageCount - 1) {
          return
        }
        this.clickHandler(this.page + 1)
      },
      handlePageSelected (selected, e) {
        e.preventDefault()
        if (this.page === selected) {
          return
        }
        this.clickHandler(selected)
      }
    }
  }
</script>

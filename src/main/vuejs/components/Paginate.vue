<template>
  <nav>
    <ul class="pagination">
      <li :class="['page-item', { disabled: firstPageSelected() }]">
        <a @click="prevPage($event)" @keyup.enter="prevPage($event)" class="page-link" tabindex="0" href="#">Previous</a>
      </li>
      <li v-for="page in pages" :class="['page-item', { active: page.selected, disabled: page.disabled }]">
        <a @click="handlePageSelected(page.index, $event)" @keyup.enter="handlePageSelected(page.index, $event)" class="page-link" tabindex="0" href="#">{{ page.content }}</a>
      </li>
      <li :class="['page-item', { disabled: lastPageSelected() }]">
        <a @click="nextPage($event)" @keyup.enter="nextPage($event)" class="page-link" tabindex="0" href="#">Next</a>
      </li>
    </ul>
  </nav>
</template>

<script>
  export default {
    props: {
      pageCount: {
        type: Number,
        required: true
      },
      initialPage: {
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
    data () {
      return {
        selected: this.initialPage
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
              selected: index === this.selected
            }
          }
        } else {
          let leftPart = this.pageRange / 2
          let rightPart = this.pageRange - leftPart
          if (this.selected < leftPart) {
            leftPart = this.selected
            rightPart = this.pageRange - leftPart
          } else if (this.selected > this.pageCount - this.pageRange / 2) {
            rightPart = this.pageCount - this.selected
            leftPart = this.pageRange - rightPart
          }
          // items logic extracted into this function
          let mapItems = index => {
            let page = {
              index: index,
              content: index + 1,
              selected: index === this.selected
            }
            if (index <= this.marginPages - 1 || index >= this.pageCount - this.marginPages) {
              items[index] = page
              return
            }
            let breakView = {
              content: '...',
              disabled: true
            }
            if ((this.selected - leftPart) > this.marginPages && items[this.marginPages] !== breakView) {
              items[this.marginPages] = breakView
            }
            if ((this.selected + rightPart) < (this.pageCount - this.marginPages - 1) && items[this.pageCount - this.marginPages - 1] !== breakView) {
              items[this.pageCount - this.marginPages - 1] = breakView
            }
            let overCount = this.selected + rightPart - this.pageCount + 1
            if (overCount > 0 && index === this.selected - leftPart - overCount) {
              items[index] = page
            }
            if ((index >= this.selected - leftPart) && (index <= this.selected + rightPart)) {
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
          if (this.selected - this.pageRange > 0) {
            selectedRangeLow = this.selected - this.pageRange
          }
          let selectedRangeHigh = this.pageCount
          if (this.selected + this.pageRange < this.pageCount) {
            selectedRangeHigh = this.selected + this.pageRange
          }
          for (let i = selectedRangeLow; i < selectedRangeHigh; i++) {
            mapItems(i)
          }
        }
        return items
      }
    },
    methods: {
      firstPageSelected () {
        return this.selected === 0
      },
      lastPageSelected () {
        return (this.selected === this.pageCount - 1) || (this.pageCount === 0)
      },
      prevPage (e) {
        if (this.selected <= 0) {
          return
        }
        this.selected--
        this.clickHandler(this.selected + 1, e)
      },
      nextPage (e) {
        if (this.selected >= this.pageCount - 1) {
          return
        }
        this.selected++
        this.clickHandler(this.selected + 1, e)
      },
      handlePageSelected (selected, e) {
        if (this.selected === selected) {
          e.preventDefault()
          return
        }
        this.selected = selected
        this.clickHandler(this.selected + 1, e)
      }
    }
  }
</script>

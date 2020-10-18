<template>
  <b-container fluid class="my-5">
    <b-row class="mb-3">
      <!-- select options for perPage -->
      <b-col sm="12" md="6" class="my-1">
        <b-form-group
          label="Per page"
          label-cols-sm="2"
          label-cols-md="3"
          label-align-sm="left"
          label-align-md="right"
          label-size="md"
          label-for="perPageSelect"
          class="mb-0 font-weight-bold"
        >
          <b-form-select
            v-model="perPage"
            id="perPageSelect"
            size="md"
            :options="pageOptions"
          ></b-form-select>
        </b-form-group>
      </b-col>

      <!-- Search Bar for filtering -->
      <b-col sm="12" md="6" class="my-1">
        <b-form-group
          label="Search"
          label-cols-sm="2"
          label-cols-md="3"
          label-align-sm="left"
          label-align-md="right"
          label-size="md"
          label-for="filterInput"
          class="mb-0 font-weight-bold"
        >
          <b-input-group size="md">
            <b-form-input
              v-model="filter"
              type="search"
              id="filterInput"
              placeholder="Type to Search"
            ></b-form-input>
            <b-input-group-append>
              <b-button :disabled="!filter" @click="filter = ''"
                >Clear</b-button
              >
            </b-input-group-append>
          </b-input-group>
        </b-form-group>
      </b-col>
    </b-row>
    <!-- The list of public posts on another row -->
    <b-table
      @row-selected="onRowSelected"
      striped
      borderless
      sticky-header="500px"
      selectable
      select-mode="single"
      :sort-by.sync="sortBy"
      :sort-desc.sync="sortDesc"
      hover
      :fields="fields"
      :items="items"
      :per-page="perPage"
      :current-page="currentPage"
      :filter="filter"
      :class="'public-post-list'"
    >
      <template v-slot:cell(post_name)="data">
        <p class="text-info">{{ data.value }}</p>
      </template>
      <template v-slot:cell(created_by)="data">
        <p class="text-primary">{{ data.value }}</p>
      </template>
      <template v-slot:cell(created_on)="data">
        <p class="text-secondary">{{ data.value }}</p>
      </template>
    </b-table>
    <!-- pagination -->
    <b-row>
      <b-col class="my-1">
        <b-pagination
          v-model="currentPage"
          :total-rows="totalRows"
          :per-page="perPage"
          align="fill"
          size="md"
          class="my-0"
        ></b-pagination>
      </b-col>
    </b-row>
  </b-container>
</template>
<script>
import dummyList from "../../store/data/dummyPublicPosts";
export default {
  methods: {
    onRowSelected(row) {
      // console.log(row[0].post_name);
      this.$router.push({
        name: "section",
        params: {
          id: row[0].id
        }
      });
    }
  },
  data() {
    return {
      perPage: 10,
      currentPage: 1,
      pageOptions: [10, 20, 50],
      totalRows: 1,
      sortBy: "created_on",
      sortDesc: true,
      filter: "",
      fields: [
        { key: "id", sortable: true },
        { key: "post_name" },
        { key: "created_by" },
        { key: "created_on", sortable: true }
      ],
      items: dummyList
    };
  },
  mounted() {
    this.totalRows = this.items.length;
  }
};
</script>
<style lang="scss" scoped>
.public-post-list {
  height: 500px;
}
</style>
<style lang="scss"></style>

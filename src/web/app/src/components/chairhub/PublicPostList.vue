<template>
  <b-container fluid class="mt-5">
    <b-row>
      <!-- select options for perPage -->
      <b-col sm="12" md="6" class="my-1">
        <b-form-group
          label="Per page"
          label-cols-sm="3"
          label-align-sm="right"
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
          label-cols-sm="3"
          label-align-sm="right"
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
      primary-key="id"
      :tbody-transition-props="transProps"
    >
      <template v-slot:cell(conference)="data">
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
          class="'public-post-list-pagination my-0'"
        ></b-pagination>
      </b-col>
    </b-row>
  </b-container>
</template>
<script>
export default {
  methods: {
    onRowSelected(row) {
      // console.log(row[0].conference);
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
      transProps: {
        name: "flip-list"
      },
      fields: [
        { key: "id", sortable: true },
        { key: "conference" },
        { key: "created_by" },
        { key: "created_on", sortable: true }
      ],
      items: [
        {
          id: 1,
          conference:
            "ICCCIT 2020: Computer, Communications and Information Technology Conference",
          created_by: "Dickerson",
          created_on: "10/10/2020"
        },
        {
          id: 2,
          conference:
            "ICCITIA 2020: Computer and Information Technologies, Innovations and Applications Conference",
          created_by: "Larsen",
          created_on: "1/10/2020"
        },
        {
          id: 3,
          conference: "ICCM 2020: Cognitive Modeling Conference",
          created_by: "Geneva",
          created_on: "2/10/2020"
        },
        {
          id: 4,
          conference:
            "ICCNSHE 2020: Computer, Network, Software and Hardware Engineering Conference",
          created_by: "Jami",
          created_on: "3/10/2020"
        },
        {
          id: 5,
          conference:
            "ICCCIT 2020: Computer, Communications and Information Technology Conference",
          created_by: "Dickerson",
          created_on: "31/09/2020"
        },
        {
          id: 6,
          conference:
            "ICCITIA 2020: Computer and Information Technologies, Innovations and Applications Conference",
          created_by: "Larsen",
          created_on: "10/10/2020"
        },
        {
          id: 7,
          conference: "ICCM 2020: Cognitive Modeling Conference",
          created_by: "Geneva",
          created_on: "10/10/2020"
        },
        {
          id: 8,
          conference:
            "ICCNSHE 2020: Computer, Network, Software and Hardware Engineering Conference",
          created_by: "Jami",
          created_on: "1/10/2020"
        },
        {
          id: 9,
          conference:
            "ICCCIT 2020: Computer, Communications and Information Technology Conference",
          created_by: "Dickerson",
          created_on: "1/12/2019"
        },
        {
          id: 10,
          conference:
            "ICCITIA 2020: Computer and Information Technologies, Innovations and Applications Conference",
          created_by: "Larsen",
          created_on: "10/10/2020"
        },
        {
          id: 11,
          conference: "ICCM 2020: Cognitive Modeling Conference",
          created_by: "Geneva",
          created_on: "10/10/2020"
        },
        {
          id: 12,
          conference:
            "ICCNSHE 2020: Computer, Network, Software and Hardware Engineering Conference",
          created_by: "Jami",
          created_on: "10/10/2020"
        }
      ]
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
<style lang="scss">
.public-post-list-pagination {
  li {
    color: aqua;
  }
  .page-link {
    color: $gray-800;
  }
}
.public-post-list {
  .flip-list-move {
    transition: all 0.5s ease-in-out;
  }
  .flip-list-enter {
    transform: scale(1);
    transition: all 0.5s ease-in-out;
  }
  .flip-list-leave {
    transform: scale(0);
    transition: all 0.5s ease-in-out;
  }
}
</style>

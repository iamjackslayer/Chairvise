<template>
  <b-container fluid class="mt-5">
    <b-row>
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
      :filter="filter"
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
  </b-container>
</template>
<script>
  export default {
    methods: {
      onRowSelected(row) {
        console.log(row[0].conference);
      },
    },
    data() {
      return {
        perPage: 10,
        pageOptions: [10, 20, 50],
        sortBy: "created_on",
        sorDesc: true,
        filter: "",
        fields: [
          { key: "conference" },
          { key: "created_by" },
          { key: "created_on", sortable: true },
        ],
        items: [
          {
            conference:
              "ICCCIT 2020: Computer, Communications and Information Technology Conference",
            created_by: "Dickerson",
            created_on: "10/10/2020",
          },
          {
            conference:
              "ICCITIA 2020: Computer and Information Technologies, Innovations and Applications Conference",
            created_by: "Larsen",
            created_on: "1/10/2020",
          },
          {
            conference: "ICCM 2020: Cognitive Modeling Conference",
            created_by: "Geneva",
            created_on: "2/10/2020",
          },
          {
            conference:
              "ICCNSHE 2020: Computer, Network, Software and Hardware Engineering Conference",
            created_by: "Jami",
            created_on: "3/10/2020",
          },
          {
            conference:
              "ICCCIT 2020: Computer, Communications and Information Technology Conference",
            created_by: "Dickerson",
            created_on: "31/09/2020",
          },
          {
            conference:
              "ICCITIA 2020: Computer and Information Technologies, Innovations and Applications Conference",
            created_by: "Larsen",
            created_on: "10/10/2020",
          },
          {
            conference: "ICCM 2020: Cognitive Modeling Conference",
            created_by: "Geneva",
            created_on: "10/10/2020",
          },
          {
            conference:
              "ICCNSHE 2020: Computer, Network, Software and Hardware Engineering Conference",
            created_by: "Jami",
            created_on: "1/10/2020",
          },
          {
            conference:
              "ICCCIT 2020: Computer, Communications and Information Technology Conference",
            created_by: "Dickerson",
            created_on: "1/12/2019",
          },
          {
            conference:
              "ICCITIA 2020: Computer and Information Technologies, Innovations and Applications Conference",
            created_by: "Larsen",
            created_on: "10/10/2020",
          },
          {
            conference: "ICCM 2020: Cognitive Modeling Conference",
            created_by: "Geneva",
            created_on: "10/10/2020",
          },
          {
            conference:
              "ICCNSHE 2020: Computer, Network, Software and Hardware Engineering Conference",
            created_by: "Jami",
            created_on: "10/10/2020",
          },
        ],
      };
    },
  };
</script>
<style lang="scss" scoped></style>

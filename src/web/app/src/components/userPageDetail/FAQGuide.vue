<template>
  <div class="accordion" role="tablist">
    <b-card no-body>
      <b-card-header
        v-b-toggle.1
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        How to use our guide?
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="1" role="tabpanel">
        <b-card-body>
          We have provided 4 different panels for our user guide:
          <ol>
            <li>Overview</li>
            <li>
              Features - To describe the features that our system can provide
            </li>
            <li>
              Instructions - To provide a detailed instructions on how to use
              ChairVise features
            </li>
            <li>FAQ - To list out commonly asked questions by user</li>
          </ol>
          To use our user guide, click on any of the tabs above to begin
          exploring.
        </b-card-body>
      </b-collapse>
    </b-card>

    <b-card no-body>
      <b-card-header
        v-b-toggle.2
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        My chart seems to overlapped
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="2" role="tabpanel">
        <b-card-body>
          Scroll all the way to the top and click "PDF" / "Powerpoint" again.
        </b-card-body>
      </b-collapse>
    </b-card>

    <b-card no-body>
      <b-card-header
        v-b-toggle.3
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        Cannot upload Review Record with no header for EasyChair
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="3" role="tabpanel">
        <b-card-body>
          Use the with header option.
        </b-card-body>
      </b-collapse>
    </b-card>
  </div>
</template>

<script>
export default {
  name: "FeatureGuide",
  data() {
    return {
      activeNames: []
    };
  }
};
</script>

<style lang="scss" scoped></style>

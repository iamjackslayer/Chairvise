<template>
  <div class="accordion" role="tablist">
    <b-card no-body>
      <b-card-header
        v-b-toggle.instruction1
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        Importing CSV Data
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="instruction1" role="tabpanel">
        <b-card-body>
          <ol>
            <li>Log in.</li>
            <li>Click on "Import Data".</li>
            <li>Fill in the forms.</li>
            <li>Upload your CSV data.</li>
            <li>Map the CSV column headers</li>
            <li>Upload!</li>
          </ol>
        </b-card-body>
      </b-collapse>
    </b-card>

    <b-card no-body>
      <b-card-header
        v-b-toggle.instruction2
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        Make Presentation
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="instruction2" role="tabpanel">
        <b-card-body>
          <ol>
            <li>Click on "Presentation".</li>
            <li>Create a new presentation.</li>
            <li>Select the visualizations you want.</li>
            <li>Click "Add New Section"</li>
            <li>In Edit Mode, users can:</li>
            <ul>
              <li>Change the name and description.</li>
              <li>Edit the mode of visualization.</li>
              <li>Add data filters for the field.</li>
              <li>Preview the changes.</li>
            </ul>
            <li>There is no limit to the number of visualizations!</li>
          </ol>
        </b-card-body>
      </b-collapse>
    </b-card>

    <b-card no-body>
      <b-card-header
        v-b-toggle.instruction3
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        Sharing Presentation
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="instruction3" role="tabpanel">
        <b-card-body>
          <ol>
            <li>Click on "SHARE"</li>
            <li>
              Enter the email address of the person you want to share it with.
            </li>
            <li>Change the permission (view or edit) given to the user.</li>
          </ol>
        </b-card-body>
      </b-collapse>
    </b-card>

    <b-card no-body>
      <b-card-header
        v-b-toggle.instruction4
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        Download presentations as PDF
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="instruction4" role="tabpanel">
        <b-card-body>
          <p>Pre-requisites: You must have create a presentation.</p>
          <ol>
            <li>Click on "Presentation".</li>
            <li>Select the presentation that you want to view.</li>
            <li>Click "PDF".</li>
            <li>And you get the presentation generated in PDF.</li>
          </ol>
        </b-card-body>
      </b-collapse>
    </b-card>
    <b-card no-body>
      <b-card-header
        v-b-toggle.instruction5
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        Download presentations as Powerpoint
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="instruction5" role="tabpanel">
        <b-card-body>
          <p>Pre-requisites: You must have create a presentation.</p>
          <ol>
            <li>Click on "Presentation".</li>
            <li>Select the presentation that you want to view.</li>
            <li>Click "Powerpoint".</li>
            <li>
              And you get the presentation generated in Powerpoint slides.
            </li>
          </ol>
        </b-card-body>
      </b-collapse>
    </b-card>
    <b-card no-body>
      <b-card-header
        v-b-toggle.instruction6
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        Manage Conference
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="instruction6" role="tabpanel">
        <b-card-body>
          <ol>
            <li>Click on "Track".</li>
            <li>Click on "Add new conferences".</li>
            <li>Create a new conference.</li>
            <li>Select the conference you want from the calendar.</li>
            <li>You get the conference details.</li>
          </ol>
        </b-card-body>
      </b-collapse>
    </b-card>
  </div>
</template>

<script>
export default {
  name: "InstructionsGuide",
  data() {
    return {
      activeNames: []
    };
  }
};
</script>

<style scoped></style>

<template>
  <div v-html="processedMarkdown"></div>
</template>

<script>
import marked from 'marked'
import DOMPurify from 'dompurify'
import highlight from 'highlight.js'
import 'highlight.js/styles/srcery.css'

export default {
  name: "MarkdownDisplay",
  computed: {
    processedMarkdown() {
      const markdownText = marked(this.text)
      const cleanMarkDownText = DOMPurify.sanitize(markdownText, {
        ALLOWED_TAGS:
            ['h2', 'h3', 'h4', 'h5',
              'em', 'strong', 'ol', 'li',
              'code', 'a', 'span', 'p', 'pre'],
        ALLOWED_ATTR: ['href', 'class']
      })
      return this.codeProcess(cleanMarkDownText);
    }
  },
  data() {
    return {}
  },
  props: ['text'],
  methods: {
    codeProcess(s) {
      if (this.isNestedCodeEnding(s)) {
        return s
      } else {
        s = this.forTags(s)
        const codePattern = /<pre><code>(.|\n)*?<\/code><\/pre>/;
        let match;
        while (match = codePattern.exec(s)) {
          let code = highlight.highlightAuto(match[0].substr(11, match[0].length - 11 - 13)).value;
          s = s.replace(codePattern, "<pre><code class=\"processed\">" + code + "</code></pre>")
        }
        return s;
      }
    },
    /**
     * html, xml, etc.
     * @param input
     */
    forTags(input) {
      input = input.replaceAll(/(&amp;)/gm, '&')
      input = input.replaceAll(/(&lt;)/gm, '<')
      input = input.replaceAll(/(&gt;)/gm, '>')
      input = input.replaceAll(/(&quot;)/gm, '\"')
      input = input.replaceAll(/(&#39;)/gm, '\'')
      input = input.replaceAll(/(&#47;)/gm, '/')
      return input;
    },
    isNestedCodeEnding(s) {
      const codeOpenTag = "&lt;pre&gt;&lt;code&gt;";
      const codeCloseTag = "&lt;/code&gt;&lt;/pre&gt;";
      const openResult = [...s.matchAll(codeOpenTag)]
      const closeResult = [...s.matchAll(codeCloseTag)]

      return openResult.length + closeResult.length !== 0;
    }
  }
}
</script>

<style scoped>

</style>
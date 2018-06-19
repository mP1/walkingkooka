# Basic Project

A collection of things missing from the JDK and other more famous projects.

## Something useful (walkingkooka.build.tostring.ToStringBuilder)

There are plenty of open source ToString helpers, like those present in Guava and Apache Commons Lang ToStringBuilder. 
However they are fairly simple and can be reduced to appending labels and values in pairs. While functional, this can 
be improved, to help make the String a little more helpful for logs and debugging purposes.

###Some interesting/useful supported ideas###

- Escaping AND/OR quoting char sources such as char arrays & CharSequences, helps spot control chars etc.
- Skipping falsey properties (nulls, zeros etc)
- Unwrapping values present Optionals
- Support for nested objects each supporting tuning of ToStringBuilderOption
- Hard limits on value and length
- Hex encoding byte and other numeric values.
- Read the javadoc & tests for more samples.

This is a very simple example using a Url record like class. Our record would have the following properties which would
be populated after parsing a URL String into its core components.

- scheme (http OR https)
- host
- port
- path
- params as a Map
- anchor

Many of the above properties will be null or empty string, the goal of ToStringBuilder is that it facilitates
rebuilding something that almost looks url, which means skipping properties that are not present.

- scheme "https://"
- host "example.com"
- params a=b c=d

```java
ToStringBuilder.create()
  .disable(ToStringBuilderOption.QUOTE) // added for clarity, sensible defaults exist.
  .disable(ToStringBuilderOption.ESCAPE)
  .separator("") // appear between value(XXX) calls
  .value(this.scheme)
  .value(this.host)
  .label(":")
  .value(this.port)
  .value(this.path)
  
   // tries to reproduce key/values joined by '&' but without URL encoding.
  .labelSeparator("=").valueSeparator("&").value(this.params)
  
  // will conditionally add the '#' if anchor is not empty(falsey) 
  .label("#").value(this.anchor)
  .build()
  
// gives ... https://example.com/?a=b&c=d 
```

Its not perfect, but it does make for more beautiful and readable strings.

## Getting the source

You can either download the source using the "ZIP" button at the top
of the github page, or you can make a clone using git:

```
git clone git://github.com/mP1/walkingkooka.git
```
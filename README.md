# Labyrinth - tool for a web site mapping

## Overview
_Labyrinth_ treats web site as graph, where each page is a graph node, connected to other nodes (pages) with 
edges - links. Mapping follows links and maps every page connected directly or indirectly to the starting page, and
prints at the end valid XML for rendering.

There are certain properties of a web site/graph, which should be taken into consideration:
 - There could be extrnal links following to other we sites, such links should be excluded and not followed;
 - Page can have links leading to the same page, they should not be followed as well;
 - Pages can be reached through multiple links, mapping for such pages should be done only once (i.e. - don't follow
 the same link more than one time);
 
 ## Build
 Application is built with SBT (Scala Build Tool) version 1.0.3 or higher.
 
 ## Usage
 Application can be run through SBT shell, or as a standalone JAR file. It takes one parameter, starting URL:
 `java -jar <jar> <url>`
 
 ## Unfinished business
 Most annoying problem I didn't find good solution for: program builds site map iteractively, follwoing each links in a separate GET
 request, which is slow. It potentiall be faster to follow links on each page in parallel, however, program has 
 to take into consideration links which has been followed previously (because there are lots of pages which are pointed from multipel
 pages on the site - all which are in the footer of the main page). In case of parallel processing, they collide, 
 and result becoming messy and difficult to understand. Potential solution would be to build a cache of visited links, and 
 let each page builder query this cache before following link.

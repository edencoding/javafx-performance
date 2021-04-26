# JavaFX Performance Tests
Simple applications to test the performance of JavaFX, for example between the Canvas and Pane objects

<h1>Tests</h1>

So far I've created the following tests:
<ol>
    <li>Canvas Vs Pane
        <ul>
            <li><b>Movement Test</b>
                <ul>
                <li>Move a large number of objects</li>
                <li>Low level of calculation required (low CPU usage). </li>
                <li>High utilisation of rendering (GPU if used by renderer) </li>
                </ul>
            </li>
            <li><b>Collision Detection Test</b>
                <ul>
                <li>Move some objects around, bash them into other objects</li>
                <li>Detect collisions between object sets, moderate CPU utilisation</li>
                <li>Moderate rendering requirements</li>
                </ul>
            </li>
            <li><b>Physics Test</b>
                <ul>
                <li>Move a <em>lot</em> of objects around, under gravity simulation</li>
                <li>Very high CPU utilisation</li>
                <li>Low rendering requirements (most pixels not painted per frame</li>
                </ul>
            </li>
            <li><b>Mouse Tracking Test (Gridded)</b>
                <ul>
                <li>Create a grid</li>
                <li>If the mouse is over a cell, highlight it red</li>
                <li>Extreme test of event-driven (Pane) or lagging rendering buffer (Canvas)</li>
                </ul>
            </li>
        </ul>
    </li>
</ol>

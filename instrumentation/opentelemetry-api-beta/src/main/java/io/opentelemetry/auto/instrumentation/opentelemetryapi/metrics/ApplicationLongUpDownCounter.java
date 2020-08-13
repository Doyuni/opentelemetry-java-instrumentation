/*
 * Copyright The OpenTelemetry Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.opentelemetry.auto.instrumentation.opentelemetryapi.metrics;

import application.io.opentelemetry.common.Labels;
import application.io.opentelemetry.metrics.LongUpDownCounter;
import io.opentelemetry.auto.instrumentation.opentelemetryapi.LabelBridging;

class ApplicationLongUpDownCounter implements LongUpDownCounter {

  private final io.opentelemetry.metrics.LongUpDownCounter agentLongUpDownCounter;

  ApplicationLongUpDownCounter(
      final io.opentelemetry.metrics.LongUpDownCounter agentLongUpDownCounter) {
    this.agentLongUpDownCounter = agentLongUpDownCounter;
  }

  io.opentelemetry.metrics.LongUpDownCounter getAgentLongUpDownCounter() {
    return agentLongUpDownCounter;
  }

  @Override
  public void add(final long delta, final Labels labels) {
    agentLongUpDownCounter.add(delta, LabelBridging.toAgent(labels));
  }

  @Override
  public BoundLongUpDownCounter bind(final Labels labels) {
    return new BoundInstrument(agentLongUpDownCounter.bind(LabelBridging.toAgent(labels)));
  }

  static class BoundInstrument implements BoundLongUpDownCounter {

    private final io.opentelemetry.metrics.LongUpDownCounter.BoundLongUpDownCounter
        agentBoundLongUpDownCounter;

    BoundInstrument(
        final io.opentelemetry.metrics.LongUpDownCounter.BoundLongUpDownCounter
            agentBoundLongUpDownCounter) {
      this.agentBoundLongUpDownCounter = agentBoundLongUpDownCounter;
    }

    @Override
    public void add(final long delta) {
      agentBoundLongUpDownCounter.add(delta);
    }

    @Override
    public void unbind() {
      agentBoundLongUpDownCounter.unbind();
    }
  }

  static class Builder implements LongUpDownCounter.Builder {

    private final io.opentelemetry.metrics.LongUpDownCounter.Builder agentBuilder;

    Builder(final io.opentelemetry.metrics.LongUpDownCounter.Builder agentBuilder) {
      this.agentBuilder = agentBuilder;
    }

    @Override
    public LongUpDownCounter.Builder setDescription(final String description) {
      agentBuilder.setDescription(description);
      return this;
    }

    @Override
    public LongUpDownCounter.Builder setUnit(final String unit) {
      agentBuilder.setUnit(unit);
      return this;
    }

    @Override
    public LongUpDownCounter.Builder setConstantLabels(final Labels constantLabels) {
      agentBuilder.setConstantLabels(LabelBridging.toAgent(constantLabels));
      return this;
    }

    @Override
    public LongUpDownCounter build() {
      return new ApplicationLongUpDownCounter(agentBuilder.build());
    }
  }
}
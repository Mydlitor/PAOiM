package pao.model;

public enum HorseCondition {
  HEALTHY {
    @Override public String toString() { return "healthy"; }
  },
  SICK {
    @Override public String toString() { return "sick"; }
  },
  TRAINING {
    @Override public String toString() { return "training"; }
  },
  QUARANTINE {
    @Override public String toString() { return "quarantine"; }
  },
  SOLD {
    @Override public String toString() { return "sold"; }
  }
}
